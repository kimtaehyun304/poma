package goinmul.sportsmanage.controller;


import goinmul.sportsmanage.domain.*;
import goinmul.sportsmanage.domain.dto.UserDto;
import goinmul.sportsmanage.domain.form.MercenaryMatchEditForm;
import goinmul.sportsmanage.domain.socialMatch.SocialMatchDto;
import goinmul.sportsmanage.domain.teamMatch.TeamMatchDto;
import goinmul.sportsmanage.domain.mercenaryMatch.*;
import goinmul.sportsmanage.domain.socialMatch.SocialMatch;
import goinmul.sportsmanage.domain.socialMatch.SocialUser;
import goinmul.sportsmanage.domain.teamMatch.TeamMatch;
import goinmul.sportsmanage.domain.teamMatch.TeamUser;
import goinmul.sportsmanage.repository.UserRepository;
import goinmul.sportsmanage.repository.mercenaryMatch.MercenaryMatchRepository;
import goinmul.sportsmanage.repository.mercenaryMatch.MercenaryMatchUserRepository;
import goinmul.sportsmanage.repository.socialMatch.SocialMatchRepository;
import goinmul.sportsmanage.repository.teamMatch.TeamMatchRepository;
import goinmul.sportsmanage.repository.teamMatch.TeamUserRepository;
import goinmul.sportsmanage.service.ReservationService;
import goinmul.sportsmanage.service.mercenaryMatch.MercenaryMatchService;
import goinmul.sportsmanage.service.mercenaryMatch.MercenaryUserService;
import goinmul.sportsmanage.service.socialMatch.SocialMatchService;
import goinmul.sportsmanage.service.socialMatch.SocialUserService;
import goinmul.sportsmanage.service.teamMatch.TeamMatchService;
import goinmul.sportsmanage.service.teamMatch.TeamUserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MatchController {

    private final UserRepository userRepository;
    private final SocialUserService socialUserService;
    private final SocialMatchService socialMatchService;
    private final SocialMatchRepository socialMatchRepository;
    private final TeamUserRepository teamUserRepository;
    private final TeamUserService teamUserService;
    private final MercenaryMatchUserRepository mercenaryMatchUserRepository;
    private final MercenaryUserService mercenaryUserService;
    private final TeamMatchRepository teamMatchRepository;
    private final TeamMatchService teamMatchService;
    private final MercenaryMatchRepository mercenaryMatchRepository;
    private final MercenaryMatchService mercenaryMatchService;
    private final ReservationService reservationService;


    //소셜 매치 조회
    @GetMapping("/socialMatch/{sports}")
    public String socialMatchList(@PathVariable Sports sports, Model model, HttpSession session,
                                  @RequestParam(required = false, defaultValue = "all") String location,
                                  @RequestParam(required = false) LocalDate date,
                                  @RequestParam(required = false) Gender gender) {

        User sessionUser = (User) session.getAttribute("user");

        if (date == null) date = LocalDate.now();
        List<SocialMatch> socialMatches = socialMatchRepository.findAllWithSocialUserAndReservationAndGroundByDateAndLocationAndGenderAndSports(date, location, gender, sports);
        List<SocialMatchDto> socialMatchesDto = socialMatches.stream().map(s -> new SocialMatchDto(s, sessionUser)).toList();

        //model.addAttribute("sports", sports); //안써도 자동으로 넣어줍니다
        model.addAttribute("location", location);
        model.addAttribute("date", date);
        model.addAttribute("gender", gender);
        model.addAttribute("socialMatches", socialMatchesDto);

        return "match/socialMatch";
    }

    //소셜 매치 참가 - 매치 생성은 ReservationController에서 예약 성공하면 매치를 함께 생성합니다.
    @PostMapping("/socialMatch/{sports}")
    @ResponseBody
    public ResponseEntity<String> joinSocialMatch(@RequestBody Long socialMatchId, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null)
            return new ResponseEntity<>("로그인 해주세요!", HttpStatus.UNAUTHORIZED);

        //saveSocialUser안에 검증 로직으로 넣을라 했는데, 다른 조건처리에 필요해서 분리했습니다
        SocialMatch socialMatch = socialMatchRepository.findWithSocialUserBySocialMatchId(socialMatchId);

        for (SocialUser socialUser : socialMatch.getSocialUsers()) {
            if (socialUser.getUser().getId().equals(sessionUser.getId())) {
                return new ResponseEntity<>("이미 신청했습니다", HttpStatus.CONFLICT);
            }
        }
        if (socialMatch.getMaxSize() == socialMatch.getSocialUsers().size()) {
            return new ResponseEntity<>("마감된 매치입니다", HttpStatus.CONFLICT);
        }
        if (!socialMatch.getGender().equals(Gender.BOTH) && !sessionUser.getGender().equals(socialMatch.getGender())) {
            return new ResponseEntity<>("모집 성별과 불일치 합니다", HttpStatus.CONFLICT);
        }

        socialUserService.saveSocialUser(SocialUser.createSocialUser(socialMatch, sessionUser));
        return new ResponseEntity<>("매치 신청 완료!", HttpStatus.CREATED);
    }

    //소셜 매치 신청 취소
    @DeleteMapping("/socialMatch/{socialMatchId}")
    @ResponseBody
    public ResponseEntity<String> cancelSocialMatch(@PathVariable Long socialMatchId, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null)
            return new ResponseEntity<>("로그인 해주세요!", HttpStatus.UNAUTHORIZED);

        SocialMatch socialMatch = socialMatchRepository.findWithSocialUserBySocialMatchId(socialMatchId);

        if (socialMatch == null)
            return new ResponseEntity<>("잘못된 요청입니다.", HttpStatus.OK);

        SocialMatchDto socialMatchDto = new SocialMatchDto(socialMatch);
        List<Long> userIdList = socialMatchDto.getUserIdList();

        if (userIdList == null || !userIdList.contains(sessionUser.getId()))
            return new ResponseEntity<>("잘못된 요청입니다.", HttpStatus.OK);

        if (userIdList.size() == 1) {
            socialMatchService.deleteSocialMatch(socialMatch); //cascade로 socialUser도 같이 삭제
            reservationService.deleteReservation(socialMatch.getReservation());
        } else {
            socialUserService.deleteSocialUser(socialMatchId, sessionUser.getId());
        }

        return new ResponseEntity<>("신청 취소 완료!", HttpStatus.OK);
    }

    //팀 매치 조회
    @GetMapping("/teamMatch/{sports}")
    public String teamMatchList(@PathVariable Sports sports, Model model, HttpSession session,
                                @RequestParam(required = false, defaultValue = "all") String location,
                                @RequestParam(required = false) LocalDate date,
                                @RequestParam(required = false) Gender gender) {

        if (date == null) date = LocalDate.now();
        User sessionUser = (User) session.getAttribute("user");

        //select 쿼리 두번으로 나눠서 만듬
        List<TeamMatch> teamMatches = teamMatchRepository.findAllWithTeamUserAndTeamReservationAndGroundByDateAndLocationAndGenderAndSports(date, location, gender, sports);
        List<TeamMatchDto> teamMatchesDto = teamMatches.stream().map(t -> new TeamMatchDto(t, sessionUser)).collect(Collectors.toList());

        model.addAttribute("teamMatches", teamMatchesDto);
        model.addAttribute("location", location);
        model.addAttribute("date", date);
        model.addAttribute("gender", gender);
        return "match/teamMatch";
    }

    //팀 매치 멤버 선택 및 조회
    @GetMapping("/teamMatch/{teamMatchId}/user/new")
    public Object teamUserList(@PathVariable Long teamMatchId, HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null)
            return new ResponseEntity<>("로그인 해주세요", HttpStatus.UNAUTHORIZED);

        if (sessionUser.getTeam() == null)
            return new ResponseEntity<>("팀에 가입해주세요", HttpStatus.CONFLICT);

        TeamMatch teamMatch = teamMatchRepository.findWithTeamUserByTeamMatchId(teamMatchId);
        TeamMatchDto teamMatchDto = new TeamMatchDto(teamMatch);

        if(teamMatchDto.getTeams().contains(sessionUser.getTeam().getName()))
            return new ResponseEntity<>("이미 팀이 신청한 경기입니다", HttpStatus.CONFLICT);

        if (teamMatch.getMaxSize() == teamMatchDto.getUserIdList().size())
            return new ResponseEntity<>("마감된 매치입니다", HttpStatus.CONFLICT);

        //모집 성별과 맞는 멤버가 부족한 경우 && 팀 멤버가 부족한 경우: 사용자가 홈페이지 구경할 수 있게 하기 위해 if처리 안했습니다.

        List<User> users = userRepository.findAllByTeamIdAndGenderAndUserIdNotIn(sessionUser.getTeam().getId(), teamMatch.getGender(), teamMatchDto.getUserIdList());
        List<UserDto> usersDto = users.stream().map(u -> new UserDto(u)).collect(Collectors.toList());

        model.addAttribute("teamMatch", teamMatchDto);
        model.addAttribute("users", usersDto);
        return "match/addTeamMemberForm";

    }

    //팀 매치 멤버 선택 저장
    @PostMapping("/teamMatch/{teamMatchId}/user/new")
    @ResponseBody
    public ResponseEntity<String> addTeamUsers(@PathVariable Long teamMatchId, @RequestBody List<Long> userIdList, HttpSession session) {

        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null)
            return new ResponseEntity<>("로그인 해주세요", HttpStatus.UNAUTHORIZED);

        if (sessionUser.getTeam() == null)
            return new ResponseEntity<>("팀에 가입해주세요", HttpStatus.CONFLICT);

        TeamMatch teamMatch = teamMatchRepository.findWithTeamUserByTeamMatchId(teamMatchId);
        TeamMatchDto teamMatchDto = new TeamMatchDto(teamMatch);

        if (teamMatch.getMaxSize().equals(teamMatch.getTeamUsers().size()))
            return new ResponseEntity<>("마감된 매치입니다", HttpStatus.CONFLICT);

        if(teamMatchDto.getTeams().contains(sessionUser.getTeam().getName()))
            return new ResponseEntity<>("이미 팀이 신청한 경기입니다", HttpStatus.CONFLICT);

        if (teamMatch.getMaxSize()/2 != userIdList.size())
            return new ResponseEntity<>(teamMatch.getMaxSize()/2 + "명을 뽑아주세요", HttpStatus.CONFLICT);

        //같은 팀 멤버를 신청하는거니까, 로그인 유저의 팀으로 퉁쳤습니다
        List<TeamUser> teamUsers = new ArrayList<>();
        for (Long userId : userIdList) {
            teamUsers.add(TeamUser.createTeamUser(teamMatch, User.createUser(userId), sessionUser.getTeam()));
        }
        teamUserService.saveTeamUsers(teamUsers);
        return new ResponseEntity<>("팀 매치 참가 완료!", HttpStatus.CREATED);
    }

    //팀 매치 선발 멤버 조회 및 수정
    //수정이 목적이라 edit 사용 (다른 이유는 멤버 조회만 하는 기능이 추가될 수 있음)
    @GetMapping("/teamMatch/{teamMatchId}/user/edit")
    public Object editTeamUsersForm(@PathVariable Long teamMatchId, HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null)
            return new ResponseEntity<>("로그인 해주세요", HttpStatus.UNAUTHORIZED);

        if (sessionUser.getTeam() == null)
            return new ResponseEntity<>("팀에 가입해주세요", HttpStatus.CONFLICT);

        TeamMatch teamMatch = teamMatchRepository.findWithTeamUserByTeamMatchIdAndTeamId(teamMatchId, sessionUser.getTeam().getId());
        TeamMatchDto teamMatchDto = new TeamMatchDto(teamMatch);
        if (!teamMatchDto.getTeams().contains(sessionUser.getTeam().getName()))
            return new ResponseEntity<>("팀이 매치에 안 참가했습니다.", HttpStatus.CONFLICT);

        List<User> users = userRepository.findByTeamIdAndGender(sessionUser.getTeam().getId(), teamMatch.getGender());
        List<UserDto> usersDto = users.stream().map(u -> new UserDto(u)).collect(Collectors.toList());
        model.addAttribute("teamMatch", teamMatchDto);
        model.addAttribute("users", usersDto);
        return "match/editTeamMemberForm";
    }

    //팀 매치 선발 멤버 수정 반영 (edit이 수정을 의미하기 때문에, put메서드 안 썼습니다)
    @PostMapping("/teamMatch/{teamMatchId}/user/edit")
    @ResponseBody
    public ResponseEntity<String> editTeamUsers(@PathVariable Long teamMatchId, @RequestBody List<Long> checkedUserIdList, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null)
            return new ResponseEntity<>("로그인 해주세요", HttpStatus.UNAUTHORIZED);

        if (sessionUser.getTeam() == null)
            return new ResponseEntity<>("팀에 가입해주세요", HttpStatus.CONFLICT);

        //자기 팀 유저만 가져옵니다
        TeamMatch teamMatch = teamMatchRepository.findWithTeamUserByTeamMatchIdAndTeamId(teamMatchId, sessionUser.getTeam().getId());
        TeamMatchDto teamMatchDto = new TeamMatchDto(teamMatch);

        if (teamMatch.getMaxSize()/2 != checkedUserIdList.size())
            return new ResponseEntity<>(teamMatch.getMaxSize()/2 + "명을 뽑아주세요", HttpStatus.CONFLICT);

        List<Long> findUserIdList = teamMatchDto.getUserIdList();
        List<Long> interSector = findUserIdList.stream().filter(checkedUserIdList::contains).toList();

        //추가할 대상 리스트 만들기 //같은 팀 멤버를 신청하는거니까, 로그인 유저의 팀으로 퉁쳤습니다
        checkedUserIdList.removeAll(interSector);
        List<TeamUser> teamUsers = new ArrayList<>();
        for (Long userId : checkedUserIdList)
            teamUsers.add(TeamUser.createTeamUser(teamMatch, User.createUser(userId), sessionUser.getTeam()));
        teamUserService.saveTeamUsers(teamUsers);

        //삭제할 대상 리스트 만들기
        findUserIdList.removeAll(interSector);
        teamUserService.deleteInUserId(findUserIdList);

        return new ResponseEntity<>("팀 매치 수정 완료!", HttpStatus.OK);
    }

    //팀 매치 멤버 일괄 취소
    @DeleteMapping("/teamMatch/{teamMatchId}/user")
    @ResponseBody
    public ResponseEntity<String> deleteTeamUsers(@PathVariable Long teamMatchId, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null)
            return new ResponseEntity<>("로그인 해주세요", HttpStatus.UNAUTHORIZED);

        if (sessionUser.getTeam() == null)
            return new ResponseEntity<>("팀에 가입해주세요", HttpStatus.CONFLICT);


        TeamMatch teamMatch = teamMatchRepository.findWithTeamUserAndTeamByTeamMatchId(teamMatchId);
        TeamMatchDto teamMatchDto = new TeamMatchDto(teamMatch);
        List<String> teams = teamMatchDto.getTeams();

        //자기 팀 유저만 가져옵니다
        List<Long> sessionUserTeamMembers = teamMatch.getTeamUsers().stream().filter(u -> u.getTeam().getId().equals(sessionUser.getTeam().getId())).map(u -> u.getUser().getId()).toList();
        //우리 팀만 매치에 참가하면 true (deleteInUserId가 실행되고 teams.size == 1 비교하는건 헷갈려서 중복되지만 이렇게 작성했습니다)
        if(teams.size() == 1 && teams.get(0).equals(sessionUser.getTeam().getName())) {
            teamMatchService.deleteTeamMatch(teamMatch);
            teamUserService.deleteInUserId(sessionUserTeamMembers);
            reservationService.deleteReservation(teamMatch.getReservation());
        }else
            teamUserService.deleteInUserId(sessionUserTeamMembers);

        return new ResponseEntity<>("팀 매치 일괄 취소 완료!", HttpStatus.OK);
    }

    //용병 매치 조회
    @GetMapping("/mercenaryMatch/{sports}")
    public String mercenaryMatchList(@PathVariable Sports sports, Model model, HttpSession session,
                                     @RequestParam(required = false, defaultValue = "all") String location,
                                     @RequestParam(required = false) LocalDate date,
                                     @RequestParam(required = false) Gender gender) {

        User sessionUser = (User) session.getAttribute("user");
        if (date == null) date = LocalDate.now();

        List<MercenaryMatch> mercenaryMatches = mercenaryMatchRepository.findAllWithMercenaryMatchUserAndTeamAndReservationAndGroundByDateAndLocationAndGenderAndSports(date, location, gender, sports);
        List<MercenaryMatchDto> mercenaryMatchesDto = mercenaryMatches.stream().map(m -> new MercenaryMatchDto(m, sessionUser)).toList();

        model.addAttribute("mercenaryMatches", mercenaryMatchesDto);
        model.addAttribute("location", location);
        model.addAttribute("date", date);
        model.addAttribute("gender", gender);
        return "match/mercenaryMatch";
    }

    //용병 매치 선발 멤버 선택 및 조회
    @GetMapping("/mercenaryMatch/{mercenaryMatchId}/user/new")
    public Object choiceMercenaryUserList(@PathVariable Long mercenaryMatchId, HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");

        MercenaryMatch mercenaryMatch = mercenaryMatchRepository.findWithMercenaryMatchUserWithTeamByMercenaryMatchId(mercenaryMatchId);
        MercenaryMatchDto mercenaryMatchDto = new MercenaryMatchDto(mercenaryMatch);

        if (sessionUser == null)
            return new ResponseEntity<>("로그인 해주세요", HttpStatus.UNAUTHORIZED);

        if (sessionUser.getTeam() == null)
            return new ResponseEntity<>("팀에 가입해주세요", HttpStatus.CONFLICT);

        List<Long> teamIdList = mercenaryMatchDto.getTeams().stream().map(t -> t.getId()).toList();
        if(teamIdList.contains(sessionUser.getTeam().getId()))
            return new ResponseEntity<>("이미 팀이 경기 신청했어요", HttpStatus.CONFLICT);

        if (mercenaryMatchDto.getMaxSize() == mercenaryMatchDto.getUserIdList().size())
            return new ResponseEntity<>("마감된 매치입니다", HttpStatus.CONFLICT);

        //상대 팀 용병 구하기 (상대 팀에 용병으로 참가한 원래 우리팀 멤버 찾기 위함)
        List<Long> userIdList = mercenaryMatch.getMercenaryMatchUsers().stream().filter(u -> !u.getTeam().getId().equals(sessionUser.getTeam().getId()))
                .filter(u -> u.getStatus().equals(UserStatus.MERCENARY)).map(u -> u.getUser().getId()).toList();

        //상대 팀에 용병으로 참가한 원래 우리팀 멤버는 제외하고 조회한다.
        List<User> users = userRepository.findAllByTeamIdAndGenderAndUserIdNotIn(sessionUser.getTeam().getId(), mercenaryMatchDto.getGender(), userIdList);

        //HttpStatus 200 자동으로 됨
        model.addAttribute("users", users);
        model.addAttribute("mercenaryMatch", mercenaryMatchDto);
        return "match/addMercenaryMemberForm";
    }

    //용병 매치 선발 멤버 선택 저장
    @PostMapping("/mercenaryMatch/{mercenaryMatchId}/user/new")
    @ResponseBody
    public ResponseEntity<String> choiceMercenaryUser(@PathVariable Long mercenaryMatchId, @RequestBody List<Long> userIdList, HttpSession session) {

        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser.getTeam() == null)
            return new ResponseEntity<>("팀에 가입해주세요", HttpStatus.CONFLICT);

        if (userIdList == null)
            return new ResponseEntity<>("팀원은 최소 한명은 있어야 해요", HttpStatus.CONFLICT);

        MercenaryMatch mercenaryMatch = mercenaryMatchRepository.findWithMercenaryMatchUserWithTeamByMercenaryMatchId(mercenaryMatchId);
        MercenaryMatchDto mercenaryMatchDto = new MercenaryMatchDto(mercenaryMatch);

        List<Long> teamIdList = mercenaryMatchDto.getTeams().stream().map(t -> t.getId()).toList();
        if(teamIdList.contains(sessionUser.getTeam().getId()))
            return new ResponseEntity<>("이미 팀이 경기 신청했어요", HttpStatus.CONFLICT);

        if(mercenaryMatch.getMaxSize()/2 < userIdList.size())
            return new ResponseEntity<>("모집 인원보다 많게 선택했어요", HttpStatus.CONFLICT);

        List<User> users = User.createUsers(userIdList);
        List<MercenaryMatchUser> mercenaryUsers = new ArrayList<>();
        for (User user : users) {
            mercenaryUsers.add(MercenaryMatchUser.createMercenaryUser(mercenaryMatch, user, sessionUser.getTeam(), UserStatus.TEAM));
        }

        //성능을 위해, 중복 검증은 공통처리 로직으로 처리했습니다. (POSTMAN을 통해 직접 보내지 않고서야 중복 데이터가 올리 없기 때문에)
        mercenaryUserService.saveMercenaryMatchUsers(mercenaryUsers);
        return new ResponseEntity<>("용병 매치 참가 완료!", HttpStatus.CREATED);
    }

    //용병 매치 선발 멤버 조회 및 수정
    @GetMapping("/mercenaryMatch/{mercenaryMatchId}/user/edit")
    public Object editMercenaryUsersForm(@PathVariable Long mercenaryMatchId, HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null)
            return new ResponseEntity<>("로그인 해주세요", HttpStatus.UNAUTHORIZED);

        if (sessionUser.getTeam() == null)
            return new ResponseEntity<>("팀에 가입해주세요", HttpStatus.CONFLICT);

        MercenaryMatch mercenaryMatch = mercenaryMatchRepository.findWithMercenaryMatchUserWithTeamByMercenaryMatchId(mercenaryMatchId);
        
        //TeamId가 하나라서 반복문이 필요없지만, 한번 반복한다해서 성능이 나빠지는 건 아니니까 재사용했습니다.
        MercenaryMatchDto mercenaryMatchDto = new MercenaryMatchDto(mercenaryMatch);

        if(mercenaryMatchDto.getTeams().isEmpty())
            return new ResponseEntity<>("팀이 매치에 안 참가했습니다.", HttpStatus.CONFLICT);

        //상대 팀 용병 구하기 (상대 팀에 용병으로 참가한 원래 우리팀 멤버 찾기 위함)
        List<Long> userIdList = mercenaryMatch.getMercenaryMatchUsers().stream().filter(u -> !u.getTeam().getId().equals(sessionUser.getTeam().getId()))
                .filter(u -> u.getStatus().equals(UserStatus.MERCENARY)).map(u -> u.getUser().getId()).toList();

        //우리팀 멤버 다 보내고 매치에 참가한 멤버는 체크박스 select 표시
        //상대 팀에 용병으로 참가한 원래 우리팀 멤버는 제외하고 조회한다.
        List<User> users = userRepository.findAllByTeamIdAndGenderAndUserIdNotIn(sessionUser.getTeam().getId(), mercenaryMatchDto.getGender(), userIdList);
        List<UserDto> membersDto = users.stream().map(u -> new UserDto(u)).collect(Collectors.toList());
        List<Long> mercenaryIdList = mercenaryMatchDto.getTeams().stream().filter(t -> t.getId().equals(sessionUser.getTeam().getId())).findFirst().get().getMercenaryIdList();

        //List<User> users = userRepository.findAllByGenderAndUserIdIn(sessionUser.getTeam().getId(), mercenaryMatchDto.getGender(), mercenaryMatchDto.getUserIdList());
        //List<UserDto> usersDto = users.stream().map(u -> new UserDto(u)).collect(Collectors.toList());
        List<User> mercenaries = userRepository.findAllByUserIdIn(mercenaryIdList);
        model.addAttribute("mercenaryMatch", mercenaryMatchDto);
        model.addAttribute("members", membersDto);
        model.addAttribute("mercenaries", mercenaries);
        return "match/editMercenaryMemberForm";
    }

    //용병 매치 선발 멤버 수정 반영 (edit이 수정을 의미하기 때문에, put메서드 안 썼습니다)
    @PostMapping("/mercenaryMatch/{mercenaryMatchId}/user/edit")
    @ResponseBody
    public ResponseEntity<String> editMercenaryMatchUsers(@PathVariable Long mercenaryMatchId, @RequestBody MercenaryMatchEditForm mercenaryMatchEditForm, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null)
            return new ResponseEntity<>("로그인 해주세요", HttpStatus.UNAUTHORIZED);

        if (sessionUser.getTeam() == null)
            return new ResponseEntity<>("팀에 가입해주세요", HttpStatus.CONFLICT);

        //자기 팀 유저만 가져옵니다
        MercenaryMatch mercenaryMatch = mercenaryMatchRepository.findWithMercenaryMatchUserWithTeamByMercenaryMatchIdAndTeamId(mercenaryMatchId, sessionUser.getTeam().getId());
        MercenaryMatchDto mercenaryMatchDto = new MercenaryMatchDto(mercenaryMatch);

        List<Long> memberIdList = mercenaryMatchEditForm.getMemberIdList();
        List<Long> mercenaryIdList = mercenaryMatchEditForm.getMercenaryIdList();

        if(mercenaryMatchDto.getTeams().isEmpty())
            return new ResponseEntity<>("팀이 매치에 안 참가했습니다.", HttpStatus.CONFLICT);

        if (memberIdList.isEmpty())
            return new ResponseEntity<>("팀원은 최소 한명은 있어야 해요", HttpStatus.CONFLICT);

        if(mercenaryMatch.getMaxSize()/2 < memberIdList.size()+mercenaryIdList.size())
            return new ResponseEntity<>("모집 인원보다 많게 선택했어요", HttpStatus.CONFLICT);

        //List<Long> findUserIdList = mercenaryMatchDto.getUserIdList();
        List<Long> findOriginalMemberIdList = mercenaryMatchDto.getTeams().get(0).getOriginalMemberIdList();
        List<Long> interSector = findOriginalMemberIdList.stream().filter(memberIdList::contains).toList();

        //원래 팀 멤버 추가,삭제
        //추가할 대상 리스트 만들기
        memberIdList.removeAll(interSector);
        List<MercenaryMatchUser> mercenaryMatchUsers = new ArrayList<>();
        for (Long userId : memberIdList)
            mercenaryMatchUsers.add(MercenaryMatchUser.createMercenaryUser(mercenaryMatch, User.createUser(userId), sessionUser.getTeam(), UserStatus.TEAM));
        if(!memberIdList.isEmpty())
            mercenaryUserService.saveMercenaryMatchUsers(mercenaryMatchUsers);

        //삭제할 대상 리스트 만들기
        findOriginalMemberIdList.removeAll(interSector);
        if(!findOriginalMemberIdList.isEmpty())
            mercenaryUserService.deleteByUserIdIn(findOriginalMemberIdList);

        //용병 삭제
        List<Long> findMercenaryIdList = mercenaryMatchDto.getTeams().get(0).getMercenaryIdList();
        findMercenaryIdList.removeAll(mercenaryIdList);

        if(!findMercenaryIdList.isEmpty())
            mercenaryUserService.deleteByUserIdIn(findMercenaryIdList);
        return new ResponseEntity<>("용병 매치 수정 완료!", HttpStatus.OK);

    }

    //용병 매치 멤버 일괄 취소 (용병 포함)
    @DeleteMapping("/mercenaryMatch/{mercenaryMatchId}/user")
    @ResponseBody
    public ResponseEntity<String> deleteMercenaryMatchUsers(@PathVariable Long mercenaryMatchId, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return new ResponseEntity<>("로그인 해주세요", HttpStatus.UNAUTHORIZED);
        }
        if (sessionUser.getTeam() == null) {
            return new ResponseEntity<>("팀에 가입해주세요", HttpStatus.CONFLICT);
        }

        MercenaryMatch mercenaryMatch = mercenaryMatchRepository.findWithMercenaryMatchUserWithTeamByMercenaryMatchId(mercenaryMatchId);
        MercenaryMatchDto mercenaryMatchDto = new MercenaryMatchDto(mercenaryMatch);

        List<Long> teams = mercenaryMatchDto.getTeams().stream().map(t -> t.getId()).toList();
        if(!teams.contains(sessionUser.getTeam().getId()))
            return new ResponseEntity<>("팀이 매치에 안 참가했습니다.", HttpStatus.CONFLICT);

        //자기 팀 유저만 가져옵니다
        List<Long> sessionUserTeamMembers = mercenaryMatch.getMercenaryMatchUsers().stream().filter(u -> u.getTeam().getId().equals(sessionUser.getTeam().getId())).map(u -> u.getUser().getId()).toList();

        //우리 팀만 매치에 참가하면 true
        if(teams.size() == 1 && teams.get(0).equals(sessionUser.getTeam().getId())) {
            mercenaryMatchService.deleteMercenaryMatch(mercenaryMatch);
            mercenaryUserService.deleteByUserIdIn(sessionUserTeamMembers);
            reservationService.deleteReservation(mercenaryMatch.getReservation());
        } else
            mercenaryUserService.deleteByUserIdIn(sessionUserTeamMembers);

        return new ResponseEntity<>("용병 매치 일괄 취소 완료!", HttpStatus.OK);
    }

    //용병 신청 저장
    @PostMapping("/mercenaryMatch/{mercenaryMatchId}/user/mercenary")
    @ResponseBody
    public ResponseEntity<String> joinMercenaryInMercenaryMatch(@PathVariable Long mercenaryMatchId, @RequestBody Long teamId, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null)
            return new ResponseEntity<>("로그인 해주세요", HttpStatus.UNAUTHORIZED);

        MercenaryMatch mercenaryMatch = mercenaryMatchRepository.findWithMercenaryMatchUserWithTeamByMercenaryMatchId(mercenaryMatchId);
        MercenaryMatchDto mercenaryMatchDto = new MercenaryMatchDto(mercenaryMatch);

        if (sessionUser.getTeam() != null && sessionUser.getTeam().getId().equals(teamId))
            return new ResponseEntity<>("본인 팀에 용병 신청 못합니다", HttpStatus.CONFLICT);

        //본인 팀에 참가했는데, 상대 팀에 용병으로 참가하면 true
        if(mercenaryMatchDto.getUserIdList().contains(sessionUser.getId()))
            return new ResponseEntity<>("이미 매치에 참가했습니다", HttpStatus.CONFLICT);

        if (!sessionUser.getGender().equals(mercenaryMatchDto.getGender()))
            return new ResponseEntity<>("모집 성별과 불일치 합니다", HttpStatus.CONFLICT);


        TeamWithMercenary teamWithMercenary = mercenaryMatchDto.getTeams().stream().filter(t -> t.getId().equals(teamId)).toList().get(0);
        if(teamWithMercenary.getMercenariesMaxSize().equals(teamWithMercenary.getMercenaryIdList().size()))
            return new ResponseEntity<>("용병 모집이 마감됐어요", HttpStatus.CONFLICT);

        if (mercenaryMatchDto.getMaxSize() == mercenaryMatchDto.getUserIdList().size())
            return new ResponseEntity<>("마감된 매치입니다", HttpStatus.CONFLICT);

        mercenaryUserService.saveMercenaryUser(MercenaryMatchUser.createMercenaryUser(mercenaryMatch, sessionUser, Team.createTeam(teamId), UserStatus.MERCENARY));
        return new ResponseEntity<>("용병 신청 완료!", HttpStatus.CREATED);
    }

    //용병 신청 취소
    @DeleteMapping("/mercenaryMatch/{mercenaryMatchId}/user/mercenary")
    @ResponseBody
    public ResponseEntity<String> deleteMercenary(@PathVariable Long mercenaryMatchId, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null)
            return new ResponseEntity<>("로그인 해주세요", HttpStatus.UNAUTHORIZED);

        //유저가 용병으로 참가했는지 조회하는 검증 로직은 공통 처리로 했습니다.
        mercenaryUserService.deleteByMercenaryMatchIdAndUserId(mercenaryMatchId, sessionUser.getId());
        return new ResponseEntity<>("용병 취소 완료!", HttpStatus.OK);
    }

}

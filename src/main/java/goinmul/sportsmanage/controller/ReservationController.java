package goinmul.sportsmanage.controller;


import goinmul.sportsmanage.domain.*;
import goinmul.sportsmanage.domain.form.ReservationForm;
import goinmul.sportsmanage.domain.mercenaryMatch.MercenaryMatch;
import goinmul.sportsmanage.domain.mercenaryMatch.MercenaryMatchUser;
import goinmul.sportsmanage.domain.mercenaryMatch.UserStatus;
import goinmul.sportsmanage.domain.socialMatch.SocialMatch;
import goinmul.sportsmanage.domain.socialMatch.SocialUser;
import goinmul.sportsmanage.domain.teamMatch.TeamMatch;
import goinmul.sportsmanage.domain.teamMatch.TeamUser;
import goinmul.sportsmanage.repository.GroundRepository;
import goinmul.sportsmanage.repository.ReservationRepository;
import goinmul.sportsmanage.repository.UserRepository;
import goinmul.sportsmanage.service.ReservationService;
import goinmul.sportsmanage.service.SportsService;
import goinmul.sportsmanage.service.UserService;
import goinmul.sportsmanage.service.mercenaryMatch.MercenaryMatchService;
import goinmul.sportsmanage.service.mercenaryMatch.MercenaryUserService;
import goinmul.sportsmanage.service.socialMatch.SocialMatchService;
import goinmul.sportsmanage.service.teamMatch.TeamMatchService;
import goinmul.sportsmanage.service.teamMatch.TeamUserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final SportsService sportsService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final SocialMatchService socialMatchService;
    private final TeamMatchService teamMatchService;
    private final MercenaryMatchService mercenaryMatchService;
    private final GroundRepository groundRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;
    private final TeamUserService teamUserService;
    private final MercenaryUserService mercenaryUserService;

    //@PathVariable를 선언해야 자동으로 model에 추가됨
    @GetMapping("/reservation/{sports}")
    public String reservationForm(@PathVariable Sports sports, Model model, HttpSession session) {
        List<Ground> grounds = groundRepository.findAllOrderByCreatedAtAsc();

        User sessionUser = (User) session.getAttribute("user");
        if(sessionUser != null){
            User user = userRepository.findOneWithTeamById(sessionUser.getId());
            model.addAttribute("user", user);
        }

        model.addAttribute("today", LocalDate.now());
        model.addAttribute("grounds", grounds);
        model.addAttribute("sports", sports);
        return "groundReservationForm";
    }

    //구장 예약
    @PostMapping("/reservation/{sports}")
    public ResponseEntity<String> reservation(@Valid @RequestBody ReservationForm reservationForm, BindingResult bindingResult,
                                              @PathVariable Sports sports, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null)
            return new ResponseEntity<>("로그인 해주세요!", HttpStatus.UNAUTHORIZED);

        if(bindingResult.hasErrors()) {
            StringBuilder message = new StringBuilder();
            for (ObjectError error : bindingResult.getAllErrors()) {
                message.append(error.getDefaultMessage()).append("\n");
            }
            message.deleteCharAt(message.length()-1);
            return new ResponseEntity<>(message.toString(), HttpStatus.CONFLICT);
        }

        Reservation findReservation = reservationRepository.findByGroundIdAndYmdAndTime(reservationForm.getGroundId(), reservationForm.getDate(), reservationForm.getTime());
        if(findReservation != null)
            return new ResponseEntity<>("해당 시간은 이미 예약됐습니다", HttpStatus.CONFLICT);

        int maxSize = reservationForm.getMaxSize();
        List<Long> userIdList = reservationForm.getUserIdList();


        List<User> users = userRepository.findWithTeamByUserIdIn(reservationForm.getUserIdList());
        Gender gender = reservationForm.getGender();

        //혼성 매치가 아닐경우 성별 검사 (GET에서 모집 성별에 맞게 필터링된 데이터를 보여주긴 하지만, 검증용 IF 입니다.)
        if(!gender.equals(Gender.BOTH)){
            for (User user : users) {
                if (user.getGender() != gender)
                    return new ResponseEntity<>("모집 성별과 안 맞는 멤버를 선택했어요", HttpStatus.CONFLICT);
            }
        }

        Ground ground = groundRepository.findOne(reservationForm.getGroundId());
        if (ground.getPrice() != 0) {
            boolean result = userService.useMoney(sessionUser, ground.getPrice());
            if (result) sessionUser.minusMoney(ground.getPrice());
            else return new ResponseEntity<>("구입할 돈이 부족합니다", HttpStatus.CONFLICT);
        }

        Reservation reservation = Reservation.createGroundReservation(ground, sessionUser, reservationForm.getDate(), reservationForm.getTime());

        switch (reservationForm.getMatch()) {
            case SOCIAL:
                reservationService.saveReservation(reservation);
                SocialUser socialUser = SocialUser.createSocialUser(sessionUser);
                SocialMatch socialMatch = SocialMatch.createSocialMatch(reservationForm.getMaxSize(), reservationForm.getGender(), sports, socialUser, reservation);
                socialMatchService.saveSocialMatch(socialMatch);
                return new ResponseEntity<>("소셜 매치 생성 완료!", HttpStatus.CREATED);

            case TEAM:
                if (sessionUser.getTeam() == null)
                    return new ResponseEntity<>("팀에 가입해 주세요", HttpStatus.CONFLICT);

                if (userIdList.isEmpty() || maxSize/2 != userIdList.size())
                    return new ResponseEntity<>(maxSize/2 + "명을 뽑아주세요", HttpStatus.CONFLICT);

                /*
                List<TeamUser> teamUsers = new ArrayList<>();
                for (User user : users) {
                    TeamUser teamUser = TeamUser.createTeamUser(user, user.getTeam());
                    teamUsers.add(teamUser);
                }
                 */

                reservationService.saveReservation(reservation);
                TeamMatch teamMatch = TeamMatch.createTeamMatch(maxSize, reservationForm.getGender(), sports, reservation);
                teamMatchService.saveTeamMatch(teamMatch);

                //같은 팀 멤버를 신청하는거니까, 로그인 유저의 팀으로 퉁쳤습니다
                List<TeamUser> teamUsers = new ArrayList<>();
                for (Long userId : userIdList)
                    teamUsers.add(TeamUser.createTeamUser(teamMatch, User.createUser(userId), sessionUser.getTeam()));
                teamUserService.saveTeamUsers(teamUsers);
                return new ResponseEntity<>("팀 매치 생성 완료!", HttpStatus.CREATED);

            case MERCENARY:
                if (sessionUser.getTeam() == null)
                    return new ResponseEntity<>("팀에 가입해 주세요", HttpStatus.CONFLICT);

                if (userIdList.isEmpty())
                    return new ResponseEntity<>("팀원은 최소 한명은 있어야 해요", HttpStatus.CONFLICT);

                if(reservationForm.getMaxSize()/2 < userIdList.size())
                    return new ResponseEntity<>("모집 인원보다 많게 선택했어요", HttpStatus.CONFLICT);

                reservationService.saveReservation(reservation);
                MercenaryMatch mercenaryMatch = MercenaryMatch.createMercenaryMatch(maxSize, reservationForm.getGender(), sports, reservation);
                mercenaryMatchService.saveMercenaryMatch(mercenaryMatch);

                List<MercenaryMatchUser> mercenaryUsers = new ArrayList<>();
                for (User user : users) {
                    MercenaryMatchUser mercenaryUser = MercenaryMatchUser.createMercenaryUser(mercenaryMatch, user, user.getTeam(), UserStatus.TEAM);
                    mercenaryUsers.add(mercenaryUser);
                }
                mercenaryUserService.saveMercenaryMatchUsers(mercenaryUsers);

                return new ResponseEntity<>("용병 매치 생성 완료!", HttpStatus.CREATED);

            default:
                return new ResponseEntity<>("에러가 발생했습니다", HttpStatus.CONFLICT);
        }
    }
}

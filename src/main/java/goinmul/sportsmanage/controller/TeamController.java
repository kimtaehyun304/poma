package goinmul.sportsmanage.controller;


import goinmul.sportsmanage.domain.CustomerSupport;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.Team;
import goinmul.sportsmanage.domain.dto.Pagination;
import goinmul.sportsmanage.repository.TeamRepository;
import goinmul.sportsmanage.service.SportsService;
import goinmul.sportsmanage.service.TeamService;
import goinmul.sportsmanage.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TeamController {

    private final UserService userService;
    private final TeamService teamService;
    private final TeamRepository teamRepository;

    @GetMapping("/team")
    public String createTeamForm() {
        return "team/createTeamForm";
    }

    @PostMapping("/team")
    @ResponseBody
    public ResponseEntity<String> createTeam(@RequestBody String name) {
        Team team = Team.createTeam(name);
        boolean result = teamService.saveTeam(team);

        if(result) return new ResponseEntity<>("팀 생성 완료", HttpStatus.OK);
        else return new ResponseEntity<>("이미 사용중인 팀 이름입니다", HttpStatus.CONFLICT);
    }

    @GetMapping("/teams")
    public String teamList(@RequestParam(required = false, defaultValue = "1") int page,
                           @RequestParam(required = false) String keyword, Model model) {
        int maxResults = 8;
        List<Team> teams = teamRepository.findAllOrderByCreatedAtAsc(page, maxResults, keyword);
        int count = Math.toIntExact(teamRepository.count(keyword));
        int pageSize = 5;
        Pagination pagination = new Pagination(page, pageSize, maxResults, count);
        model.addAttribute("pagination", pagination);
        model.addAttribute("teams", teams);
        return "team/teamList";
    }

    @PostMapping("/teams")
    public String joinTeam(HttpSession session, @RequestParam Long teamId){
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            return "pleaseLogin";
        }

        Team findTeam = teamRepository.findOne(teamId); //세션에 팀 이름을 반영하기위함
        sessionUser.changeTeam(findTeam);

        userService.changeTeam(sessionUser, Team.createTeam(teamId));
        return "redirect:/user/myPage";
    }


}

package goinmul.sportsmanage.api;

import goinmul.sportsmanage.domain.Authority;
import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.dto.UserDto;
import goinmul.sportsmanage.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserApi {

    private final UserRepository userRepository;

    //구장예약 팀 멤버 조회 api
    @GetMapping("/api/team/users")
    public Object findByTeamIdAndGender(@RequestParam(required = false) Gender gender, HttpSession session){
        //@RequestParam Long teamId,
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null)
            return new ResponseEntity<>("로그인 해주세요!", HttpStatus.UNAUTHORIZED);

        if(gender == null)
            return new ResponseEntity<>("gender 파라미터가 누락됐습니다.", HttpStatus.CONFLICT);

        List<User> users = userRepository.findByTeamIdAndGender(sessionUser.getTeam().getId(), gender);
        return users.stream().map(u -> new UserDto(u)).collect(Collectors.toList());
    }

    //멘토 조회 API
    @GetMapping("/api/mentors")
    public Object findByName(@RequestParam(required = false) String name){
        List<User> users = userRepository.findByName(name);
        return users.stream().map(u -> new UserDto(u)).collect(Collectors.toList());
    }

}

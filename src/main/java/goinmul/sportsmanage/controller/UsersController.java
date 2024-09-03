package goinmul.sportsmanage.controller;


import goinmul.sportsmanage.domain.Reservation;
import goinmul.sportsmanage.domain.SportsInfo;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.dto.JoinedMatchesDto;
import goinmul.sportsmanage.domain.form.UserForm;
import goinmul.sportsmanage.repository.ReservationRepository;
import goinmul.sportsmanage.repository.UserRepository;
import goinmul.sportsmanage.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UsersController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ReservationRepository reservationRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/user/signup")
    public String createUserForm() {
        return "user/signup";
    }

    //USER 테이블 authority 컬럼 기본값 'USER'
    @PostMapping("/user/signup")
    @ResponseBody
    public ResponseEntity<String> createUser(@RequestBody User user) {
        user.changePassword(bCryptPasswordEncoder.encode(user.getPassword()));
        boolean result = userService.saveUser(user);

        if (result)
            return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);
        else
            return new ResponseEntity<>("회원가입 실패 - 이미 사용중인 아이디거나 사용중인 이름 입니다", HttpStatus.CONFLICT);
    }

    @GetMapping("/user/signin")
    public String loginForm(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");

        /*
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser != null)
            //return new ResponseEntity<>("<script>alert('이미 로그인한 상태입니다'); window.location.href= document.referrer;</script>", HttpStatus.CONFLICT);
            return new ResponseEntity<>("이미 로그인한 상태입니다", HttpStatus.CONFLICT);

         */

        return "user/signin";

    }

    @PostMapping("/user/signin")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody UserForm userForm, HttpSession session) {
        User user = userRepository.findWithTeamByLoginId(userForm.getLoginId());
        if(userForm.getLoginId().equals("guest") || userForm.getLoginId().equals("guest2")) {
            session.setAttribute("user", user);
            //jwt 발급
            return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
        }else {
            if (user != null && bCryptPasswordEncoder.matches(userForm.getPassword(), user.getPassword())) {
                session.setAttribute("user", user);
                return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("로그인 실패", HttpStatus.CONFLICT);
    }

    @PostMapping("/user/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null)
            session.invalidate(); // 세션 무효화

        return "redirect:/";
    }

    @GetMapping("/user/myPage")
    public String myPage(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null)
            return "pleaseLogin";
        else {
            User user = userRepository.findOneWithTeamById(sessionUser.getId());
            model.addAttribute("user", user);
        }
        return "user/myPage";
    }

    //매치 참가 이력 조회
    @GetMapping("/user/matches")
    public String joinMatchList(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null)
            return "pleaseLogin";

        JoinedMatchesDto joinedMatchesDto = userRepository.findJoinedMatchesDtoByUserId(sessionUser.getId());
        model.addAttribute("joinedMatchesDto", joinedMatchesDto);
        model.addAttribute("user", sessionUser);
        return "user/joinedMatches";
    }

    @GetMapping("/user/reservations")
    public String reservationList(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null)
            return "pleaseLogin";

        List<Reservation> reservations = reservationRepository.findWithGroundByUserId(sessionUser.getId());

        model.addAttribute("reservations", reservations);
        model.addAttribute("user", sessionUser);
        return "user/reservations";
    }

    @GetMapping("/paymentMobile")
    public Object addMoneyMobile(@RequestParam(required = false) String imp_uid, @RequestParam(required = false) String merchant_uid, @RequestParam(required = false) String url,
                                 HttpSession session) throws IOException {
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null)
            return new ResponseEntity<>("로그인 해주세요!", HttpStatus.UNAUTHORIZED);

        byte[] bytes = FileCopyUtils.copyToByteArray(new ClassPathResource("portOneKeySecret.json").getInputStream());
        String json = new String(bytes, StandardCharsets.UTF_8);

        //1단계 - 엑세스 토큰 가져오기
        Map<String, Object> accessTokenResponse = RestClient.create().post()
                .uri("https://api.iamport.kr/users/getToken")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (accessTokenResponse == null)
            return new ResponseEntity<>("결제 실패!", HttpStatus.CONFLICT);

        String accessToken = (String) ((Map<?, ?>) accessTokenResponse.get("response")).get("access_token");

        //2단계 - 결제내역 단건 조회 (클라이언트 위변조 검증)
        Map<String, Object> paymentResponse = RestClient.create().get()
                .uri("https://api.iamport.kr/payments/{imp_uid}", imp_uid)
                .header("Authorization", accessToken)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (paymentResponse == null)
            return new ResponseEntity<>("결제 실패!", HttpStatus.CONFLICT);

        Integer amount = (Integer) ((Map<?, ?>) paymentResponse.get("response")).get("amount");

        //3단계 - DB 반영 & 세션 반영
        userService.chargeMoney(sessionUser, amount);
        sessionUser.plusMoney(amount);

        return "redirect:"+url;
    }

    @PostMapping("/payment")
    @ResponseBody
    public ResponseEntity<String> addMoney(@RequestBody String imp_uid, HttpSession session) throws IOException {
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null)
            return new ResponseEntity<>("로그인 해주세요!", HttpStatus.UNAUTHORIZED);

        byte[] bytes = FileCopyUtils.copyToByteArray(new ClassPathResource("portOneKeySecret.json").getInputStream());
        String json = new String(bytes, StandardCharsets.UTF_8);

        //1단계 - 엑세스 토큰 가져오기
        Map<String, Object> accessTokenResponse = RestClient.create().post()
                .uri("https://api.iamport.kr/users/getToken")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (accessTokenResponse == null)
            return new ResponseEntity<>("결제 실패!", HttpStatus.CONFLICT);

        String accessToken = (String) ((Map<?, ?>) accessTokenResponse.get("response")).get("access_token");

        //2단계 - 결제내역 단건 조회 (클라이언트 위변조 검증)
        Map<String, Object> paymentResponse = RestClient.create().get()
                .uri("https://api.iamport.kr/payments/{imp_uid}", imp_uid)
                .header("Authorization", accessToken)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (paymentResponse == null)
            return new ResponseEntity<>("결제 실패!", HttpStatus.CONFLICT);

        Integer amount = (Integer) ((Map<?, ?>) paymentResponse.get("response")).get("amount");

        //3단계 - DB 반영 & 세션 반영
        userService.chargeMoney(sessionUser, amount);
        sessionUser.plusMoney(amount);

        //클라이언트가 요청한 값을 충전하기 때문에, 위변조 검증 안 필요합니다.
        return new ResponseEntity<>("결제 및 충전 완료!", HttpStatus.CREATED);
    }

    @GetMapping("/user/findAccount")
    public String findAccountForm() {
        return "user/findAccount";
    }

    @PostMapping("/user/findAccount")
    @ResponseBody
    public ResponseEntity<String> findAccount(@RequestBody String email) {
        if (email == null) return new ResponseEntity<>("이메일을 적어주세요", HttpStatus.CONFLICT);

        boolean result = userService.changePasswordAndSendMail(email);
        if (result)
            return new ResponseEntity<>("메일로 인증 정보를 담은 메일이 발송되었습니다. 메일이 보이지 않으면 스팸보관함을 확인하시기 바랍니다", HttpStatus.OK);
        else
            return new ResponseEntity<>("해당 이메일로 가입한 계정이 없습니다", HttpStatus.CONFLICT);

    }

}
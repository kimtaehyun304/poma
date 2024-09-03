package goinmul.sportsmanage.controller;


import goinmul.sportsmanage.domain.Authority;
import goinmul.sportsmanage.domain.Ground;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.form.AuthorityForm;
import goinmul.sportsmanage.domain.form.GroundForm;
import goinmul.sportsmanage.repository.GroundRepository;
import goinmul.sportsmanage.repository.UserRepository;
import goinmul.sportsmanage.service.AdminService;
import goinmul.sportsmanage.service.GroundService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final GroundRepository groundRepository;
    private final GroundService groundService;
    private final AdminService adminService;

    //관리자 페이지
    @GetMapping("/admin")
    public String adminPage(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if(sessionUser == null || sessionUser.getAuthority().equals(Authority.USER)){
            model.addAttribute("message", "관리자만 접근할 수 있어요");
            return "alertPage";
        }
        return "admin/adminPage";
    }

    //관리자 페이지 - 계정 권한 부여
    @GetMapping("/admins")
    public String authorityList(Model model,HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if(sessionUser == null || sessionUser.getAuthority().equals(Authority.USER)){
            model.addAttribute("message", "관리자만 접근할 수 있어요");
            return "alertPage";
        }

        List<User> users = userRepository.findExcludeSuperAdmin();
        model.addAttribute("users", users);
        return "admin/userAuthorityList";
    }

    //관리자 페이지 - 계정 권한 부여
    @PostMapping("/admins")
    @ResponseBody
    public ResponseEntity<String> grantAuthority(@RequestBody AuthorityForm authorityForm, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if(sessionUser == null || !sessionUser.getAuthority().equals(Authority.SUPER_ADMIN))
            return new ResponseEntity<>("SUPER_ADMIN 만 사용할 수 있어요", HttpStatus.FORBIDDEN);

        User user = User.createUser(authorityForm.getId());
        Authority authority = authorityForm.getAuthority();
        boolean result = adminService.grantAuthority(user, authority);

        if(result) {
            return new ResponseEntity<>("권한 변경 성공!", HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("권한 변경 실패!", HttpStatus.CONFLICT);
        }

    }

    //관리자 페이지 - 구장 관리
    @GetMapping("/grounds")
    public String groundList(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if(sessionUser == null || sessionUser.getAuthority().equals(Authority.USER)){
            model.addAttribute("message", "관리자만 접근할 수 있어요");
            return "alertPage";
        }
        List<Ground> grounds = groundRepository.findAllOrderByCreatedAtAsc();
        model.addAttribute("grounds", grounds);
        return "admin/groundList";
    }

    //관리자 페이지 - 구장 생성
    @GetMapping("/ground")
    public String groundFrom(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if(sessionUser == null || sessionUser.getAuthority().equals(Authority.USER)){
            model.addAttribute("message", "관리자만 접근할 수 있어요");
            return "alertPage";
        }
        return "admin/createGroundForm";
    }

    //관리자 페이지 - 구장 생성
    @PostMapping("/ground")
    @ResponseBody
    public ResponseEntity<String> createGround(@Valid @RequestBody GroundForm groundForm, BindingResult bindingResult, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if(sessionUser == null || sessionUser.getAuthority().equals(Authority.USER))
            return new ResponseEntity<>("관리자만 접근할 수 있어요", HttpStatus.FORBIDDEN);

        if(bindingResult.hasErrors()) {
            StringBuilder message = new StringBuilder();
            for (ObjectError error : bindingResult.getAllErrors()) {
                message.append(error.getDefaultMessage()).append("\n");
            }
            message.deleteCharAt(message.length()-1);
            return new ResponseEntity<>(message.toString(), HttpStatus.CONFLICT);
        }

        Ground ground = Ground.createGround(groundForm.getName(), groundForm.getLocation(), groundForm.getPrice());
        boolean result = groundService.saveGround(ground);

        if(result)
            return new ResponseEntity<>("구장 추가 성공!", HttpStatus.CREATED);
        else
            return new ResponseEntity<>("구장 추가 실패! 위치+이름 데이터가 이미 존재합니다", HttpStatus.CONFLICT);
    }

    //관리자 페이지 - 구장 수정
    @GetMapping("/ground/{id}")
    public String updateGroundFrom(@PathVariable Long id, Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if(sessionUser == null || sessionUser.getAuthority().equals(Authority.USER)){
            model.addAttribute("message", "관리자만 접근할 수 있어요");
            return "alertPage";
        }

        Ground ground = groundRepository.findOne(id);
        model.addAttribute("ground", ground);

        return "admin/updateGroundForm";
    }

    //관리자 페이지 - 구장 수정
    @PostMapping("/ground/{id}")
    @ResponseBody
    public ResponseEntity<String> updateGround(@Valid @RequestBody GroundForm groundForm, BindingResult bindingResult, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if(sessionUser == null || sessionUser.getAuthority().equals(Authority.USER))
            return new ResponseEntity<>("관리자만 접근할 수 있어요", HttpStatus.FORBIDDEN);

        if(bindingResult.hasErrors()){
            StringBuilder message = new StringBuilder();
            for (ObjectError error : bindingResult.getAllErrors()) {
                message.append(error.getDefaultMessage()).append("\n");
            }
            return new ResponseEntity<>(message.toString(), HttpStatus.CONFLICT);
        }

        boolean result = groundService.changeGround(groundForm.getId(), groundForm.getLocation(), groundForm.getName(), groundForm.getPrice());

        if(result)
            return new ResponseEntity<>("구장 수정 성공!", HttpStatus.CREATED);
        else
            return new ResponseEntity<>("구장 수정 실패! 위치+이름 데이터가 이미 존재합니다", HttpStatus.CONFLICT);

    }

}

package goinmul.sportsmanage.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class HomeController {


    @GetMapping({"/", "/;{param}"})
    public String index(@PathVariable(required = false) String param) {
        return "mainPage";
    }

    @GetMapping("/test")
    public String test(HttpServletRequest session) {
        return "mainPage";
    }



}

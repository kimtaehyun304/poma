package goinmul.sportsmanage.controller;


import goinmul.sportsmanage.domain.CustomerSupport;
import goinmul.sportsmanage.domain.MentorReview;
import goinmul.sportsmanage.domain.SportsInfo;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.dto.MentorReviewDto;
import goinmul.sportsmanage.domain.dto.Pagination;
import goinmul.sportsmanage.domain.form.ReviewForm;
import goinmul.sportsmanage.repository.MentorReviewRepository;
import goinmul.sportsmanage.repository.UserRepository;
import goinmul.sportsmanage.service.SportsService;
import jakarta.servlet.http.HttpServletRequest;
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
public class MentorMenteeController {

    private final SportsService sportsService;
    private final UserRepository userRepository;
    private final MentorReviewRepository mentorReviewRepository;

    @GetMapping("/reviews")
    public String reviewList(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam String sports,
                             @RequestParam(required = false) String name, Model model) {
        int maxResults = 10;
        List<MentorReviewDto> posts = mentorReviewRepository.findAllWithUserGroupByMentorId(page, maxResults, name);
        int count = mentorReviewRepository.sizeCountGroupByMentorId(name);
        
        int pageSize = 5;
        Pagination pagination = new Pagination(page, pageSize, maxResults, count);
        model.addAttribute("posts", posts);
        model.addAttribute("pagination", pagination);
        model.addAttribute("sports", sports);

        return "mentorMentee/reviewList";
    }

    @GetMapping("/reviews/users/{mentorId}")
    public String reviewDetail(@PathVariable Long mentorId, @RequestParam(required = false, defaultValue = "1") int page, @RequestParam String sports, Model model) {
        int maxResults = 10;
        List<MentorReview> mentorReviews = mentorReviewRepository.findByMentorId(mentorId, page, maxResults);
        int count = Math.toIntExact(mentorReviewRepository.countByMentorId(mentorId));
        int pageSize = 5;
        Pagination pagination = new Pagination(page, pageSize, maxResults, count);
        model.addAttribute("mentorId", mentorId);
        model.addAttribute("mentorReviews", mentorReviews);
        model.addAttribute("pagination", pagination);
        model.addAttribute("sports", sports);
        return "mentorMentee/reviewDetail";
    }

    @GetMapping("/reviews/form")
    public String createReviewForm(@RequestParam String sports, @RequestParam(required = false) Long mentorId, Model model) {

        if(mentorId != null) {
            User user = userRepository.findOne(mentorId);
            //List<User> users = userRepository.findAll();
            model.addAttribute("user", user);
        }
        model.addAttribute("sports", sports);
        model.addAttribute("mentorId", mentorId);
        return "mentorMentee/createMentorReviewForm";
    }

    @PostMapping("/reviews/form")
    @ResponseBody
    public Object createReview(@RequestBody @Valid ReviewForm reviewForm, BindingResult bindingResult, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null)
            return new ResponseEntity<>("로그인 해주세요!", HttpStatus.UNAUTHORIZED);

        if(bindingResult.hasErrors()) {
            StringBuilder message = new StringBuilder();
            for (ObjectError error : bindingResult.getAllErrors()) {
                message.append(error.getDefaultMessage()).append("\n");
            }
            message.deleteCharAt(message.length()-1);
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
           // return new ResponseEntity<>("<script>alert('%s'); history.back()</script>".formatted(message), HttpStatus.CONFLICT);
            //return "redirect:/reviews/form?mentorId=%d?sports=%s".formatted(mentorId, sports);
        }
        User mentor = User.createUser(reviewForm.getMentorId());
        MentorReview mentorMentee = MentorReview.createMentorMentee(mentor, sessionUser, reviewForm.getScore(), reviewForm.getDescription());
        mentorReviewRepository.save(mentorMentee);
        return new ResponseEntity<>("리뷰 저장 완료!", HttpStatus.CREATED);
        //return "redirect:/reviews/users/%d?sports=%s".formatted(reviewForm.getMentorId(), reviewForm.getSports());
    }

}

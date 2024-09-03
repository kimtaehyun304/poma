package goinmul.sportsmanage.controller;

import goinmul.sportsmanage.domain.Authority;
import goinmul.sportsmanage.domain.Comments;
import goinmul.sportsmanage.domain.CustomerSupport;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.dto.Pagination;
import goinmul.sportsmanage.domain.form.CommentsForm;
import goinmul.sportsmanage.domain.form.CustomerPostForm;
import goinmul.sportsmanage.repository.CommentsRepository;
import goinmul.sportsmanage.repository.CustomerSupportRepository;
import goinmul.sportsmanage.service.CommentsService;
import goinmul.sportsmanage.service.CustomerSupportService;
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
public class CustomerSupportController {

    private final CustomerSupportRepository customerSupportRepository;
    private final CustomerSupportService customerSupportService;
    private final CommentsRepository commentsRepository;
    private final CommentsService commentsService;

    //문의 글 작성 폼
    @GetMapping("/customer")
    public String customerPostForm() {
        return "customerSupport/createCustomerPostForm";
    }

    //문의 글 저장
    @PostMapping("/customer")
    @ResponseBody
    public ResponseEntity<String> createCustomerPost(@Valid @RequestBody CustomerPostForm customerPostForm, BindingResult bindingResult, HttpSession session) {

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

        CustomerSupport customerSupport = CustomerSupport.createPost(sessionUser, customerPostForm.getTitle(), customerPostForm.getContent(), customerPostForm.isSecret());

        // 포스트 저장
        customerSupportService.saveCustomerPost(customerSupport);

        return new ResponseEntity<>("문의 작성 완료!", HttpStatus.CREATED);
    }

    //문의 글 작성 완료 페이지
    @GetMapping("/customer/ok")
    public String customerPostOk() {
        return "customerSupport/customerPostSuccess";
    }

    //고객센터 게시글 리스트
    @GetMapping("/customers")
    public String customerPostList(@RequestParam(required = false, defaultValue = "1") int page,
                                   @RequestParam(required = false) String category, @RequestParam(required = false) String keyword, Model model) {
        int maxResults = 10;
        List<CustomerSupport> posts = customerSupportRepository.findAllWithUserOrderByRegdateDesc(page, maxResults, category, keyword);
        int count = Math.toIntExact(customerSupportRepository.count(category, keyword));
        int pageSize = 5;
        Pagination pagination = new Pagination(page, pageSize, maxResults, count);
        model.addAttribute("posts", posts);
        model.addAttribute("pagination", pagination);
        return "customerSupport/customerPostList";
    }

    @GetMapping("/customers/{id}")
    public Object customerPostDetail(@PathVariable Long id, @RequestParam(required = false, defaultValue = "1") int page, Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        CustomerSupport post = customerSupportRepository.findOne(id);

        //공개 글, 자기가 작성한 글, 관리자면 TRUE
        if (!post.isSecret() || (sessionUser != null && (post.getUser().getId().equals(sessionUser.getId()) || !sessionUser.getAuthority().equals(Authority.USER))) ) {
            //HTTP STATUS 200 자동으로 됨
            int maxResults = 10;
            List<Comments> comments = commentsRepository.findAllWithChildWithUserByCustomerSupportIdOrderByCreatedAtAsc(id, page, maxResults);
            int count = Math.toIntExact(commentsRepository.countByCustomerSupportIdAndParentId(id));
            int pageSize = 5;
            Pagination pagination = new Pagination(page, pageSize, maxResults, count);
            model.addAttribute("post", post);
            model.addAttribute("comments", comments);
            model.addAttribute("pagination", pagination);
            return "customerSupport/customerPostDetail";
        } else {
            return new ResponseEntity<>("비밀 글입니다", HttpStatus.CONFLICT);
            //return new ResponseEntity<>("<script>alert('비밀 글입니다'); history.back()</script>", HttpStatus.CONFLICT);
        }
    }

    //댓글 저장
    @PostMapping("/customers/{customerSupportId}/comments")
    @ResponseBody
    public Object saveComment(@PathVariable Long customerSupportId, @RequestBody @Valid CommentsForm commentsForm, BindingResult bindingResult, HttpSession session) {

        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null)
            return new ResponseEntity<>("로그인 해주세요!", HttpStatus.UNAUTHORIZED);

        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.toString(), HttpStatus.CONFLICT);
        }

        Comments comment;
        if(commentsForm.getParentId() == null)
             comment = new Comments(new CustomerSupport(customerSupportId), null, sessionUser, commentsForm.getContent());
        else
             comment = new Comments(new CustomerSupport(customerSupportId), new Comments(commentsForm.getParentId()), sessionUser, commentsForm.getContent());
        commentsService.saveComment(comment);


        return new ResponseEntity<>(comment.getId(), HttpStatus.CREATED);

    /*
        if (sessionUser == null) {
            return new ResponseEntity<>("로그인 해주세요!", HttpStatus.UNAUTHORIZED);
        }

        if(bindingResult.hasErrors()) {
            StringBuilder message = new StringBuilder();
            for (ObjectError error : bindingResult.getAllErrors()) {
                message.append(error.getDefaultMessage()).append("\n");
            }
            message.deleteCharAt(message.length()-1);
            return new ResponseEntity<>(message.toString(), HttpStatus.CONFLICT);
        }

        CustomerSupport customerSupport = CustomerSupport.createPost(sessionUser, customerPostForm.getTitle(), customerPostForm.getContent(), customerPostForm.isSecret());

        // 포스트 저장
        customerSupportService.saveCustomerPost(customerSupport);

     */


    }

}

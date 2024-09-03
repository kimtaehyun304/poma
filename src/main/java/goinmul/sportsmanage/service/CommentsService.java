package goinmul.sportsmanage.service;

import goinmul.sportsmanage.domain.Comments;
import goinmul.sportsmanage.domain.CustomerSupport;
import goinmul.sportsmanage.repository.CommentsRepository;
import goinmul.sportsmanage.repository.CustomerSupportRepository;
import goinmul.sportsmanage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentsService {

    private final UserRepository userRepository;
    private final CommentsRepository commentsRepository;
    @Transactional
    public void saveComment(Comments comments) {
        commentsRepository.save(comments);
    }

}

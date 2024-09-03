package goinmul.sportsmanage.service.socialMatch;


import goinmul.sportsmanage.domain.socialMatch.SocialUser;
import goinmul.sportsmanage.repository.socialMatch.SocialUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SocialUserService {

    private final SocialUserRepository socialUserRepository;

    @Transactional
    public void saveSocialUser(SocialUser socialUser) {
        socialUserRepository.save(socialUser);
    }

    @Transactional
    public void deleteSocialUser(Long socialMatchId, Long sessionUserId) {
        socialUserRepository.delete(socialMatchId, sessionUserId);
    }

    //변경 감지
    @Transactional
    public void removeSocialUser(List<Long> userIdList, Long sessionUserId) {
        userIdList.remove(sessionUserId);
    }


}

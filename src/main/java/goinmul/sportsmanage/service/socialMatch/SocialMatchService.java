package goinmul.sportsmanage.service.socialMatch;


import goinmul.sportsmanage.domain.socialMatch.SocialMatch;
import goinmul.sportsmanage.repository.socialMatch.SocialMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SocialMatchService {

    private final SocialMatchRepository socialMatchRepository;

    @Transactional
    public void saveSocialMatch(SocialMatch socialMatch) {
        socialMatchRepository.save(socialMatch);
    }

    @Transactional
    public void deleteSocialMatch(SocialMatch socialMatch) {
        socialMatchRepository.delete(socialMatch);
    }

}

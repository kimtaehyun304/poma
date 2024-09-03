package goinmul.sportsmanage.service.mercenaryMatch;

import goinmul.sportsmanage.domain.mercenaryMatch.*;
import goinmul.sportsmanage.domain.teamMatch.TeamMatch;
import goinmul.sportsmanage.repository.mercenaryMatch.MercenaryMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MercenaryMatchService {

    private final MercenaryMatchRepository mercenaryMatchRepository;

    @Transactional
    public void saveMercenaryMatch(MercenaryMatch mercenaryMatch) {
        mercenaryMatchRepository.save(mercenaryMatch);
    }

    @Transactional
    public void deleteMercenaryMatch(MercenaryMatch mercenaryMatch) {
        mercenaryMatchRepository.delete(mercenaryMatch);
    }

}

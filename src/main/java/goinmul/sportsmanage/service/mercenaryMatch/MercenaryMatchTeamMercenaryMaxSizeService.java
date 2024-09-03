package goinmul.sportsmanage.service.mercenaryMatch;


import goinmul.sportsmanage.domain.mercenaryMatch.MercenaryMatchTeamMercenaryMaxSize;
import goinmul.sportsmanage.repository.mercenaryMatch.MercenaryMatchTeamMercenaryMaxSizeRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MercenaryMatchTeamMercenaryMaxSizeService {

    private final MercenaryMatchTeamMercenaryMaxSizeRepository mercenaryMatchTeamMercenaryMaxSizeRepository;

    @Transactional
    public void save(MercenaryMatchTeamMercenaryMaxSize mercenaryTeam) {
        mercenaryMatchTeamMercenaryMaxSizeRepository.save(mercenaryTeam);
    }




}

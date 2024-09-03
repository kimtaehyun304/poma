package goinmul.sportsmanage.service.teamMatch;

import goinmul.sportsmanage.domain.teamMatch.TeamMatch;
import goinmul.sportsmanage.repository.ReservationRepository;
import goinmul.sportsmanage.repository.teamMatch.TeamMatchRepository;
import goinmul.sportsmanage.repository.teamMatch.TeamUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TeamMatchService {

    private final TeamMatchRepository teamMatchRepository;

    @Transactional
    public void saveTeamMatch(TeamMatch teamMatch) {
        teamMatchRepository.save(teamMatch);
    }

    @Transactional
    public void deleteTeamMatch(TeamMatch teamMatch) {
        teamMatchRepository.delete(teamMatch);
    }




}

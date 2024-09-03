package goinmul.sportsmanage.service;

import goinmul.sportsmanage.domain.Team;
import goinmul.sportsmanage.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;
    @Transactional
    public boolean saveTeam(Team team) {
        boolean result = false;
        Team findTeam = teamRepository.findTeamByName(team.getName());
        if(findTeam == null) {
            teamRepository.save(team);
            result = true;
        }
        return result;
    }

}

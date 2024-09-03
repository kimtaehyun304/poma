package goinmul.sportsmanage.service.teamMatch;

import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.Team;
import goinmul.sportsmanage.domain.teamMatch.TeamMatch;
import goinmul.sportsmanage.domain.teamMatch.TeamUser;
import goinmul.sportsmanage.repository.teamMatch.TeamUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor

public class TeamUserService {

    private final TeamUserRepository teamUserRepository;
    private final JdbcTemplate jdbcTemplate;
    @Transactional
    public void saveTeamUser(TeamUser teamUser) {
        teamUserRepository.save(teamUser);
    }

    @Transactional
    public void saveTeamUsers(List<TeamUser> teamUsers) {
        jdbcTemplate.batchUpdate("INSERT INTO team_user(team_match_id, user_id, team_id) values (?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, teamUsers.get(i).getTeamMatch().getId());
                ps.setLong(2, teamUsers.get(i).getUser().getId());
                ps.setLong(3, teamUsers.get(i).getTeam().getId());
            }
            @Override
            public int getBatchSize() {
                return teamUsers.size();
            }
        });
    }

    @Transactional
    public void deleteInUserId(List<Long> userIdList) {
        teamUserRepository.deleteInUserId(userIdList);
    }


}

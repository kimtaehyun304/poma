package goinmul.sportsmanage.service.mercenaryMatch;

import goinmul.sportsmanage.domain.Team;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.mercenaryMatch.MercenaryMatch;
import goinmul.sportsmanage.domain.mercenaryMatch.MercenaryMatchUser;
import goinmul.sportsmanage.domain.mercenaryMatch.UserStatus;
import goinmul.sportsmanage.domain.teamMatch.TeamMatch;
import goinmul.sportsmanage.domain.teamMatch.TeamUser;
import goinmul.sportsmanage.repository.mercenaryMatch.MercenaryMatchUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MercenaryUserService {

    private final MercenaryMatchUserRepository mercenaryUserRepository;
    private final JdbcTemplate jdbcTemplate;
    @Transactional
    public void saveMercenaryUser(MercenaryMatchUser mercenaryUser) {
        mercenaryUserRepository.save(mercenaryUser);
    }

    @Transactional
    public void deleteByMercenaryMatchIdAndUserId(Long mercenaryMatchId, Long userId) {
        mercenaryUserRepository.deleteByMercenaryMatchIdAndUserId(mercenaryMatchId, userId);
    }


    @Transactional
    public void saveMercenaryMatchUsers(List<MercenaryMatchUser> mercenaryMatchUsers) {

        jdbcTemplate.batchUpdate("INSERT INTO mercenary_match_user(mercenary_match_id, user_id, team_id, status) values (?, ?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, mercenaryMatchUsers.get(i).getMercenaryMatch().getId());
                ps.setLong(2, mercenaryMatchUsers.get(i).getUser().getId());
                ps.setLong(3, mercenaryMatchUsers.get(i).getTeam().getId());
                ps.setString(4, mercenaryMatchUsers.get(i).getStatus().name());
            }
            @Override
            public int getBatchSize() {
                return mercenaryMatchUsers.size();
            }
        });
    }

    @Transactional
    public void deleteByUserIdIn(List<Long> userIdList) {
        mercenaryUserRepository.deleteByUserIdIn(userIdList);
    }

}

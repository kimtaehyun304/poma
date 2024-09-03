package goinmul.sportsmanage.repository.teamMatch;


import goinmul.sportsmanage.domain.teamMatch.TeamUser;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamUserRepository {

    private final EntityManager em;

    public void save(TeamUser teamUser) {
        em.persist(teamUser);
    }

    public void deleteInUserId(List<Long> userIdList) {
        em.createQuery("delete from TeamUser t where t.user.id in :userIdList")
                .setParameter("userIdList", userIdList)
                .executeUpdate();
    }

    public TeamUser findOne(Long id) {
        return em.find(TeamUser.class, id);
    }

    public List<TeamUser> findAll() {
        return em.createQuery("select t from TeamUser t", TeamUser.class).getResultList();
    }

    public List<TeamUser> findAllByTeamMatchId(Long teamMatchId) {
        return em.createQuery("select t from TeamUser t where t.teamMatch.id = :teamMatchId", TeamUser.class)
                .setParameter("teamMatchId", teamMatchId)
                .getResultList();
    }

    public List<TeamUser> findWithTeamWithTeamMatchWithReservationWithGroundByUserId(Long userId) {
        return em.createQuery("select t from TeamUser t join fetch t.team join fetch t.teamMatch join fetch t.teamMatch.reservation join fetch t.teamMatch.reservation.ground " +
                        "where t.user.id =: userId", TeamUser.class)
                .setParameter("userId", userId)
                .getResultList();
    }



}

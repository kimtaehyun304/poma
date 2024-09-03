package goinmul.sportsmanage.repository.teamMatch;


import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.Sports;
import goinmul.sportsmanage.domain.teamMatch.TeamMatch;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamMatchRepository {

    private final EntityManager em;

    public void save(TeamMatch teamMatch) {
        em.persist(teamMatch);
    }

    public void delete(TeamMatch teamMatch) {
        em.remove(teamMatch);
    }

    public TeamMatch findOne(Long id) {
        return em.find(TeamMatch.class, id);
    }

    public List<TeamMatch> findAll() {
        return em.createQuery("select t from TeamMatch t", TeamMatch.class).getResultList();
    }

    //팀 매치 선발 멤버 수정 반영
    public TeamMatch findWithTeamUserAndTeamByTeamMatchId(Long id) {
        List<TeamMatch> findTeamMatch = em.createQuery("select t from TeamMatch t join fetch t.teamUsers u join fetch  u.team where t.id = :id", TeamMatch.class)
                .setParameter("id", id)
                .getResultList();

        return findTeamMatch.isEmpty() ? null : findTeamMatch.get(0);
    }

    //팀 매치 선발 멤버 수정 반영
    public TeamMatch findWithTeamUserByTeamMatchId(Long id) {
        List<TeamMatch> findTeamMatch = em.createQuery("select t from TeamMatch t join fetch t.teamUsers where t.id = :id", TeamMatch.class)
                .setParameter("id", id)
                .getResultList();

        return findTeamMatch.isEmpty() ? null : findTeamMatch.get(0);
    }

    public TeamMatch findWithTeamUserByTeamMatchIdAndTeamId(Long teamMatchId, Long teamId) {
        List<TeamMatch> findTeamMatch = em.createQuery("select t from TeamMatch t join fetch t.teamUsers u join fetch u.team t2 " +
                        "where t.id = :teamMatchId and t2.id = :teamId", TeamMatch.class)
                .setParameter("teamMatchId", teamMatchId)
                .setParameter("teamId", teamId)
                .getResultList();

        return findTeamMatch.isEmpty() ? null : findTeamMatch.get(0);
    }

    // 팀 매치 조회
    public List<TeamMatch> findAllWithTeamUserAndTeamReservationAndGroundByDateAndLocationAndGenderAndSports(LocalDate date, String location, Gender gender, Sports sports) {
        List<TeamMatch> teamMatches;
        if (location.equals("all") && gender == null) {
            teamMatches = em.createQuery("select t from TeamMatch t join fetch t.teamUsers u join fetch u.team join fetch t.reservation r join fetch r.ground " +
                            "where r.reservationYmd = :date AND t.sports =: sports order by r.reservationTime desc", TeamMatch.class)
                    .setParameter("date", date)
                    .setParameter("sports", sports)
                    .getResultList();
        } else if (location.equals("all")) {
            teamMatches = em.createQuery("select t from TeamMatch t join fetch t.teamUsers u join fetch u.team join fetch t.reservation r join fetch r.ground " +
                            "where r.reservationYmd = :date AND t.gender = :gender AND t.sports =: sports order by r.reservationTime desc", TeamMatch.class)
                    .setParameter("date", date)
                    .setParameter("gender", gender)
                    .setParameter("sports", sports)
                    .getResultList();
        } else if (gender == null) {
            teamMatches = em.createQuery("select t from TeamMatch t join fetch t.teamUsers u join fetch u.team join fetch t.reservation r join fetch r.ground " +
                            "where r.reservationYmd = :date AND r.ground.location = :location AND t.sports =: sports order by r.reservationTime desc", TeamMatch.class)
                    .setParameter("date", date)
                    .setParameter("location", location)
                    .setParameter("sports", sports)
                    .getResultList();
        } else {
            teamMatches = em.createQuery("select t from TeamMatch t join fetch t.teamUsers u join fetch u.team join fetch t.reservation r join fetch r.ground " +
                            "where r.reservationYmd = :date AND r.ground.location = :location AND t.gender = :gender AND t.sports =: sports " +
                            "order by t.reservation.reservationTime desc", TeamMatch.class)
                    .setParameter("date", date)
                    .setParameter("location", location)
                    .setParameter("gender", gender)
                    .setParameter("sports", sports)
                    .getResultList();
        }
        return teamMatches;
    }

}





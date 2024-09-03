package goinmul.sportsmanage.repository.mercenaryMatch;


import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.Sports;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.mercenaryMatch.*;
import goinmul.sportsmanage.domain.teamMatch.TeamMatch;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MercenaryMatchRepository {

    private final EntityManager em;
    private final MercenaryMatchTeamMercenaryMaxSizeRepository mercenaryMatchTeamMercenaryMaxSizeRepository;

    public void save(MercenaryMatch mercenaryMatch) {
        em.persist(mercenaryMatch);
    }

    public void delete(MercenaryMatch mercenaryMatch) {
        em.remove(mercenaryMatch);
    }

    public MercenaryMatch findOne(Long id) {
        return em.find(MercenaryMatch.class, id);
    }

    public List<MercenaryMatch> findAll() {
        return em.createQuery("select m from MercenaryMatch m", MercenaryMatch.class).getResultList();
    }

    //용병 매치 조회
    public List<MercenaryMatch> findAllWithMercenaryMatchUserAndTeamAndReservationAndGroundByDateAndLocationAndGenderAndSports(LocalDate date, String location, Gender gender, Sports sports) {
        List<MercenaryMatch> mercenaryMatches;
        if (location.equals("all") && gender == null) {
            mercenaryMatches = em.createQuery("select m from MercenaryMatch m  join fetch m.mercenaryMatchUsers mu join fetch mu.team join fetch m.reservation r join fetch r.ground " +
                            "where r.reservationYmd = :date AND m.sports =: sports " +
                            "order by r.reservationTime desc", MercenaryMatch.class)
                    .setParameter("date", date)
                    .setParameter("sports", sports)
                    .getResultList();
        } else if (location.equals("all")) {
            mercenaryMatches = em.createQuery("select m from MercenaryMatch m join fetch m.mercenaryMatchUsers mu join fetch mu.team join fetch m.reservation r join fetch r.ground " +
                            "where r.reservationYmd = :date AND m.gender = :gender AND m.sports =: sports " +
                            "order by r.reservationTime desc", MercenaryMatch.class)
                    .setParameter("date", date)
                    .setParameter("gender", gender)
                    .setParameter("sports", sports)
                    .getResultList();
        } else if (gender == null) {
            mercenaryMatches = em.createQuery("select m from MercenaryMatch m join fetch m.mercenaryMatchUsers mu join fetch mu.team join fetch m.reservation r join fetch r.ground " +
                            "where r.reservationYmd = :date AND r.ground.location = :location AND m.sports =: sports " +
                            "order by r.reservationTime desc", MercenaryMatch.class)
                    .setParameter("date", date)
                    .setParameter("location", location)
                    .setParameter("sports", sports)
                    .getResultList();
        } else {
            mercenaryMatches = em.createQuery("select m from MercenaryMatch m join fetch m.mercenaryMatchUsers mu join fetch mu.team join fetch m.reservation r join fetch r.ground " +
                            "where r.reservationYmd = :date AND r.ground.location = :location AND m.gender = :gender AND m.sports =: sports " +
                            "order by r.reservationTime desc", MercenaryMatch.class)
                    .setParameter("date", date)
                    .setParameter("location", location)
                    .setParameter("gender", gender)
                    .setParameter("sports", sports)
                    .getResultList();
        }
        return mercenaryMatches;
    }

    public MercenaryMatch findWithMercenaryMatchUserWithTeamByMercenaryMatchId(Long mercenaryMatchId) {
        List<MercenaryMatch> mercenaryMatches = em.createQuery("select m from MercenaryMatch m join fetch m.mercenaryMatchUsers u join fetch u.team " +
                        "where m.id = :mercenaryMatchId", MercenaryMatch.class)
                .setParameter("mercenaryMatchId", mercenaryMatchId)
                .getResultList();
        return mercenaryMatches.isEmpty() ? null : mercenaryMatches.get(0);
    }

    public MercenaryMatch findWithMercenaryMatchUserByMercenaryMatchId(Long mercenaryMatchId) {
        List<MercenaryMatch> mercenaryMatches = em.createQuery("select m from MercenaryMatch m join fetch m.mercenaryMatchUsers " +
                        "where m.id = :mercenaryMatchId", MercenaryMatch.class)
                .setParameter("mercenaryMatchId", mercenaryMatchId)
                .getResultList();
        return mercenaryMatches.isEmpty() ? null : mercenaryMatches.get(0);
    }



    //신청한 멤버 조회
    public MercenaryMatch findWithMercenaryMatchUserWithTeamByMercenaryMatchIdAndTeamId(Long mercenaryMatchId, Long teamId) {
        List<MercenaryMatch> mercenaryMatches = em.createQuery("select m from MercenaryMatch m join fetch m.mercenaryMatchUsers u join fetch u.team " +
                        "where m.id = :mercenaryMatchId and u.team.id = :teamId", MercenaryMatch.class)
                .setParameter("mercenaryMatchId", mercenaryMatchId)
                .setParameter("teamId", teamId)
                .getResultList();
        return mercenaryMatches.isEmpty() ? null : mercenaryMatches.get(0);
    }


/*
    public List<TeamWithMercenary> findTeamWithMercenary(Long mercenaryMatchId){
        em.createQuery("select t from Team t join fetch t.mercenaryMatchUsers u join fetch t.mercenaryMaxSizes ms " +
                "where ms.mercenaryMatch.id = :mercenaryMatchId and u.status = :status", TeamWithMercenary.class)
                .setParameter("mercenaryMatchId", mercenaryMatchId)
                .setParameter("status", UserStatus.MERCENARY)
                .getResultList();

    }

    //용병 신청 저장 전 if용
    public MercenaryMatchDto findWithMercenaryMaxSizeAndMercenaryUser(Long mercenaryMatchId) {
        MercenaryMatch mercenaryMatch = findWithMercenaryUserByMercenaryMatchId(mercenaryMatchId);
        List<MercenaryMatchTeamMercenaryMaxSize> mercenaryMaxSizeWithTeam = mercenaryMatchTeamMercenaryMaxSizeRepository.findWithTeamByMercenaryMatchId(mercenaryMatchId);

        MercenaryMatchDto mercenaryMatchDto = new MercenaryMatchDto(mercenaryMatch);

        //MercenaryMatchdDto의 teamWithMercenary 할당
        for (MercenaryMatchTeamMercenaryMaxSize mmswm : mercenaryMaxSizeWithTeam) {
            TeamWithMercenary teamWithMercenary = TeamWithMercenary.createTeamWithMercenary(mmswm.getTeam(), mmswm.getMaxSize());
            mercenaryMatchDto.getTeamsWithMercenary().add(teamWithMercenary);
        }

        List<TeamWithMercenary> teamWithMercenaryList = mercenaryMatchDto.getTeamsWithMercenary();
        for (MercenaryMatchUser mercenaryMatchUser : mercenaryMatch.getMercenaryMatchUsers()) {
            if (mercenaryMatchUser.getStatus().equals(UserStatus.MERCENARY)) {
                if (mercenaryMatchUser.getTeam().getId().equals(teamWithMercenaryList.get(0).getTeam().getId())) {
                    teamWithMercenaryList.get(0).getMercenaries().add(mercenaryMatchUser);
                } else teamWithMercenaryList.get(1).getMercenaries().add(mercenaryMatchUser);
            }
        }

        return mercenaryMatchDto;
    }


    //용병 매치 선발 멤버 조회 (두 곳에서 사용)
    public MercenaryMatchDto findWithMercenaryMaxSizeAndMercenaryUser(Long mercenaryMatchId) {
        MercenaryMatch mercenaryMatch = findWithMercenaryUserByMercenaryMatchId(mercenaryMatchId);
        List<MercenaryMatchTeamMercenaryMaxSize> mercenaryMaxSizeWithTeam = mercenaryMatchTeamMercenaryMaxSizeRepository.findWithTeamByMercenaryMatchId(mercenaryMatchId);

        MercenaryMatchDto mercenaryMatchDto = new MercenaryMatchDto(mercenaryMatch);

        //MercenaryMatchdDto의 teamWithMercenary 할당
        for (MercenaryMatchTeamMercenaryMaxSize mmswm : mercenaryMaxSizeWithTeam) {
            TeamWithMercenary teamWithMercenary = TeamWithMercenary.createTeamWithMercenary(mmswm.getTeam(), mmswm.getMaxSize());
            mercenaryMatchDto.getTeamsWithMercenary().add(teamWithMercenary);
        }

        List<TeamWithMercenary> teamWithMercenaryList = mercenaryMatchDto.getTeamsWithMercenary();
        for (MercenaryMatchUser mercenaryMatchUser : mercenaryMatch.getMercenaryMatchUsers()) {
            if (mercenaryMatchUser.getStatus().equals(UserStatus.MERCENARY)) {
                if (mercenaryMatchUser.getTeam().getId().equals(teamWithMercenaryList.get(0).getTeam().getId())) {
                    teamWithMercenaryList.get(0).getMercenaries().add(mercenaryMatchUser);
                } else teamWithMercenaryList.get(1).getMercenaries().add(mercenaryMatchUser);
            }
        }

        return mercenaryMatchDto;
    }

*/
}

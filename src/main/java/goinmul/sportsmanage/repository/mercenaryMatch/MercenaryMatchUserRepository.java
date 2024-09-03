package goinmul.sportsmanage.repository.mercenaryMatch;


import goinmul.sportsmanage.domain.mercenaryMatch.MercenaryMatchUser;
import goinmul.sportsmanage.domain.mercenaryMatch.UserStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MercenaryMatchUserRepository {

    private final EntityManager em;

    public void save(MercenaryMatchUser mercenaryUser) {
        em.persist(mercenaryUser);
    }

    public void deleteByMercenaryMatchIdAndUserId(Long mercenaryMatchId, Long userId) {
        em.createQuery("delete from MercenaryMatchUser t where t.mercenaryMatch.id = :mercenaryMatchId and t.user.id = :userId")
                .setParameter("mercenaryMatchId", mercenaryMatchId)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    public void deleteByUserIdIn(List<Long> userIdList) {
        em.createQuery("delete from MercenaryMatchUser t where t.user.id in :userIdList")
                .setParameter("userIdList", userIdList)
                .executeUpdate();
    }

    public MercenaryMatchUser findOne(Long id) {
        return em.find(MercenaryMatchUser.class, id);
    }

    public List<MercenaryMatchUser> findAll() {
        return em.createQuery("select m from MercenaryMatchUser m", MercenaryMatchUser.class).getResultList();
    }


    public List<MercenaryMatchUser> findAllByMercenaryMatchId(Long id) {
        return em.createQuery("select m from MercenaryMatchUser m where m.mercenaryMatch.id =: id", MercenaryMatchUser.class)
                .setParameter("id", id)
                .getResultList();
    }
    /*
    public List<MercenaryMatchUser> findAllWithMercenaryMatchByMercenaryMatchId(Long id) {
        return em.createQuery("select m from MercenaryMatchUser m join fetch m.mercenaryMatch where m.mercenaryMatch.id =: id", MercenaryMatchUser.class)
                .setParameter("id", id)
                .getResultList();
    }

     */

    public MercenaryMatchUser findByMercenaryMatchIdAndUserId(Long mercenaryMatchId, Long userId) {
        MercenaryMatchUser mercenaryUser = null;
        List<MercenaryMatchUser> result = em.createQuery("select m from MercenaryMatchUser m where m.mercenaryMatch.id =: mercenaryMatchId AND m.user.id =:userId", MercenaryMatchUser.class)
                .setParameter("mercenaryMatchId", mercenaryMatchId)
                .setParameter("userId", userId)
                .getResultList();
        if (!result.isEmpty()) {mercenaryUser = result.get(0);}
        return mercenaryUser;
    }

    public List<MercenaryMatchUser> findWithTeamWithMercenaryMatchWithReservationWithGroundByUserId(Long userId) {
        return em.createQuery("select m from MercenaryMatchUser m join fetch m.team join fetch m.mercenaryMatch join fetch m.mercenaryMatch.reservation join fetch m.mercenaryMatch.reservation.ground " +
                        "where m.user.id =: userId", MercenaryMatchUser.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}

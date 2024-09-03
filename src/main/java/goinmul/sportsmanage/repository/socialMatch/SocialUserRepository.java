package goinmul.sportsmanage.repository.socialMatch;

import goinmul.sportsmanage.domain.socialMatch.SocialMatch;
import goinmul.sportsmanage.domain.socialMatch.SocialUser;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SocialUserRepository {

    private final EntityManager em;

    public void save(SocialUser socialUser) {
        em.persist(socialUser);
    }

    public void delete(Long socialMatchId, Long userId) {
        em.createQuery("delete from SocialUser s where s.socialMatch.id = :socialMatchId and s.user.id = :userId")
                .setParameter("socialMatchId", socialMatchId)
                .setParameter("userId", userId)
                .executeUpdate();
    }


    public SocialUser findOne(Long id) {
        return em.find(SocialUser.class, id);
    }

    public List<SocialUser> findAll() {
        return em.createQuery("select s from SocialUser s", SocialUser.class).getResultList();
    }


    public List<SocialUser> findWithSocialMatchWithReservationWithGroundByUserId(Long userId) {
        return em.createQuery("select s from SocialUser s join fetch s.socialMatch join fetch s.socialMatch.reservation join fetch s.socialMatch.reservation.ground" +
                        " where s.user.id =: userId", SocialUser.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}

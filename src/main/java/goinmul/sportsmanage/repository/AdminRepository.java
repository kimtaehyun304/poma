package goinmul.sportsmanage.repository;


import goinmul.sportsmanage.domain.Authority;
import goinmul.sportsmanage.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AdminRepository {

    private final EntityManager em;

    public void updateAuthority(User user, Authority authority) {
        em.createQuery("update User u set u.authority = :authority where u.id = :userId")
                .setParameter("authority", authority)
                .setParameter("userId", user.getId())
                .executeUpdate();
    }

}
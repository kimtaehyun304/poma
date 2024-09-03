package goinmul.sportsmanage.repository;


import goinmul.sportsmanage.domain.Comments;
import goinmul.sportsmanage.domain.CustomerSupport;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentsRepository {

    private final EntityManager em;

    public void save(Comments comments) {
        em.persist(comments);
    }

    public Comments findOne(Long id) {
        return em.find(Comments.class, id);
    }
    /*
    public List<Comments> findAllWithUserByCustomerSupportIdOrderByCreatedAtDesc(Long id, int page, int maxResults) {
        return em.createQuery("select c from Comments c join fetch c.user where c.customerSupport.id = :id order by c.createdAt DESC ", Comments.class)
                .setParameter("id", id)
                .setFirstResult((page-1)*maxResults)
                .setMaxResults(maxResults)
                .getResultList();
    }
     */
    /*
    public List<Comments> findAllWithChildWithUserByCustomerSupportIdOrderByCreatedAtDesc(Long id, int page, int maxResults) {
        return em.createQuery("select c from Comments c left join fetch c.children h join fetch c.user where c.customerSupport.id = :id " +
                        "order by c.createdAt DESC, h.createdAt ASC ", Comments.class)
                .setParameter("id", id)
                .setFirstResult((page-1)*maxResults)
                .setMaxResults(maxResults)
                .getResultList();
    }
     */

    public List<Comments> findAllWithChildWithUserByCustomerSupportIdOrderByCreatedAtAsc(Long id, int page, int maxResults) {
        return em.createQuery("select c from Comments c join fetch c.user where c.customerSupport.id = :id and c.parent.id is null " +
                        "order by c.createdAt asc", Comments.class)
                .setParameter("id", id)
                .setFirstResult((page-1)*maxResults)
                .setMaxResults(maxResults)
                .getResultList();
    }


    public Long countByCustomerSupportIdAndParentId(Long id) {
        List<Long> count = em.createQuery("select count(c) from Comments c where c.customerSupport.id = :id and c.parent.id is null ", Long.class)
                .setParameter("id", id)
                .getResultList();
        return count.isEmpty() ? 0 : count.get(0);
    }


}

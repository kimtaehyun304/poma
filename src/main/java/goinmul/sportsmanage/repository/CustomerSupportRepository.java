package goinmul.sportsmanage.repository;


import goinmul.sportsmanage.domain.Authority;
import goinmul.sportsmanage.domain.CustomerSupport;
import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomerSupportRepository {

    private final EntityManager em;

    public void save(CustomerSupport customerSupport) {
        em.persist(customerSupport);
    }

    public CustomerSupport findOne(Long id) {
        return em.find(CustomerSupport.class, id);
    }

    public List<CustomerSupport> findAllWithUserOrderByRegdateDesc(int page, int maxResults, String category, String keyword) {
        if(keyword == null){
            return em.createQuery("select c from CustomerSupport c join fetch c.user order by c.regdate DESC ", CustomerSupport.class)
                    .setFirstResult((page - 1) * maxResults)
                    .setMaxResults(maxResults)
                    .getResultList();
        }else {
            if(category.equals("name")){
                return em.createQuery("select c from CustomerSupport c join fetch c.user where c.user.name like concat('%', :keyword, '%') order by c.regdate DESC ", CustomerSupport.class)
                        .setParameter("keyword", keyword)
                        .setFirstResult((page - 1) * maxResults)
                        .setMaxResults(maxResults)
                        .getResultList();
            }else {
                return em.createQuery("select c from CustomerSupport c join fetch c.user where c.content like concat('%', :keyword, '%') order by c.regdate DESC ", CustomerSupport.class)
                        .setParameter("keyword", keyword)
                        .setFirstResult((page - 1) * maxResults)
                        .setMaxResults(maxResults)
                        .getResultList();
            }
        }

    }

    public Long count(String category, String keyword) {
        List<Long> count;
        if(keyword == null){
            count = em.createQuery("select count(c) from CustomerSupport c", Long.class).getResultList();
        }else {
            if(category.equals("name")){
                count = em.createQuery("select count(c) from CustomerSupport c where c.user.name like concat('%', :keyword, '%')", Long.class)
                        .setParameter("keyword", keyword)
                        .getResultList();
            }else {
                count = em.createQuery("select count(c) from CustomerSupport c where c.content like concat('%', :keyword, '%')", Long.class)
                        .setParameter("keyword", keyword)
                        .getResultList();
            }
        }
        return count.isEmpty() ? 0 : count.get(0);
    }

    public CustomerSupport findWithCommentsOrderByCreatedAtDesc(int page, int maxResults) {
        List<CustomerSupport> customerSupports = em.createQuery("select c from CustomerSupport c", CustomerSupport.class)
                .setFirstResult((page-1) * maxResults)
                .setMaxResults(maxResults)
                .getResultList();
        return customerSupports.isEmpty() ? null : customerSupports.get(0);
    }

}

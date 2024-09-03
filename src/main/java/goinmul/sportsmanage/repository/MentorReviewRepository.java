package goinmul.sportsmanage.repository;


import goinmul.sportsmanage.domain.MentorReview;
import goinmul.sportsmanage.domain.dto.MentorReviewDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MentorReviewRepository {

    private final EntityManager em;

    @Transactional
    public void save(MentorReview mentorMentee) {
        em.persist(mentorMentee);
    }

    public MentorReview findOne(Long id) {
        return em.find(MentorReview.class, id);
    }

    public List<MentorReview> findAll() {
        return em.createQuery("select m from MentorReview m", MentorReview.class).getResultList();
    }

    //실행하면 잘 됩니다 (멘토 리뷰리스트)
    public List<MentorReviewDto> findAllWithUserGroupByMentorId(int page, int maxResults, String name) {
        List<MentorReviewDto> reviews;
        if(name == null){
            reviews = em.createQuery("select new goinmul.sportsmanage.domain.dto.MentorReviewDto(m, round(avg(m.score), 1), count(m.id)) from MentorReview m " +
                                    "join fetch m.mentor group by m.mentor.id order by avg(m.score) desc",
                            MentorReviewDto.class)
                    .setFirstResult((page - 1) * maxResults)
                    .setMaxResults(maxResults)
                    .getResultList();
        }else {
            reviews = em.createQuery("select new goinmul.sportsmanage.domain.dto.MentorReviewDto(m, round(avg(m.score), 1), count(m.id)) from MentorReview m " +
                                    "join fetch m.mentor where m.mentor.name like concat('%', :name, '%') group by m.mentor.id order by avg(m.score) desc",
                            MentorReviewDto.class)
                    .setParameter("name", name)
                    .setFirstResult((page - 1) * maxResults)
                    .setMaxResults(maxResults)
                    .getResultList();
        }
        return reviews;
    }

    public int sizeCountGroupByMentorId(String name) {
        List<Long> count;
        if(name == null){
            count = em.createQuery("select count(m.id) from MentorReview m group by m.mentor.id", Long.class)
                    .getResultList();

        }else {
            count = em.createQuery("select count(m.id) from MentorReview m where m.mentor.name like concat('%', :name, '%') group by m.mentor.id", Long.class)
                    .setParameter("name", name)
                    .getResultList();
        }
        return count.isEmpty() ? 0 : count.size();
    }

    public List<MentorReview> findByMentorId(Long mentorId, int page, int maxResults) {
        return em.createQuery("select m from MentorReview m where m.mentor.id = :mentorId", MentorReview.class)
                .setParameter("mentorId", mentorId)
                .setFirstResult((page-1)*maxResults)
                .setMaxResults(maxResults)
                .getResultList();
    }

    public Long countByMentorId(Long mentorId) {
        List<Long> count = em.createQuery("select count(m.id) from MentorReview m where m.mentor.id = : mentorId", Long.class)
                .setParameter("mentorId", mentorId)
                .getResultList();
        return count.isEmpty() ? null : count.get(0);
    }


}

package goinmul.sportsmanage.repository;



import goinmul.sportsmanage.domain.CustomerSupport;
import goinmul.sportsmanage.domain.Team;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamRepository {

    private final EntityManager em;

    public void save(Team team) {
        em.persist(team);
    }

    public Team findOne(Long id) {
        return em.find(Team.class, id);
    }

    public List<Team> findAll(){
        return em.createQuery("select t from Team t", Team.class).getResultList();
    }

    public List<Team> findAllOrderByCreatedAtAsc(int page, int maxResults, String keyword) {

        if(keyword == null) {
            return em.createQuery("select t from Team t order by t.createdAt asc", Team.class)
                    .setFirstResult((page - 1) * maxResults)
                    .setMaxResults(maxResults)
                    .getResultList();
        }else {
            return em.createQuery("select t from Team t where t.name like concat('%', :keyword ,'%') order by t.createdAt asc", Team.class)
                    .setParameter("keyword", keyword)
                    .setFirstResult((page - 1) * maxResults)
                    .setMaxResults(maxResults)
                    .getResultList();
        }
    }

    public Long count(String keyword) {
        List<Long> count;

        if(keyword == null) {
            count = em.createQuery("select count(t) from Team t", Long.class).getResultList();
        }else {
            count = em.createQuery("select count(t) from Team t where t.name like concat('%', :keyword ,'%')", Long.class)
                    .setParameter("keyword", keyword)
                    .getResultList();
        }

        return count.isEmpty() ? 0 : count.get(0);
    }


    public Team findTeamByName(String name){
        List<Team> teams = em.createQuery("select t from Team t where t.name = :name", Team.class)
                .setParameter("name", name).getResultList();
        return teams.isEmpty() ? null : teams.get(0);
    }

}

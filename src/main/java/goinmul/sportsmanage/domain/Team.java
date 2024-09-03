package goinmul.sportsmanage.domain;

import goinmul.sportsmanage.domain.mercenaryMatch.MercenaryMatchTeamMercenaryMaxSize;
import goinmul.sportsmanage.domain.mercenaryMatch.MercenaryMatchUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    private String name;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "team")
    @BatchSize(size = 1000)
    private List<User> users = new ArrayList<>();


    //용병 신청 post에서 사용험
    public static Team createTeam(Long id){
        Team team = new Team();
        team.setId(id);
        return team;
    }

    public static Team createTeam(String name){
        Team team = new Team();
        team.setName(name);
        team.setCreatedAt(LocalDateTime.now());
        return team;
    }

}

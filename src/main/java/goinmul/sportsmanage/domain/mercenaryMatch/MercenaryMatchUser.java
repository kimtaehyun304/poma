package goinmul.sportsmanage.domain.mercenaryMatch;



import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MercenaryMatchUser {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mercenary_user_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "mercenary_match_id")
    private MercenaryMatch mercenaryMatch;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    public static MercenaryMatchUser createMercenaryUser(MercenaryMatch mercenaryMatch, User user, Team team, UserStatus status){
        MercenaryMatchUser mercenaryUser = new MercenaryMatchUser();
        mercenaryUser.setMercenaryMatch(mercenaryMatch);
        mercenaryUser.setUser(user);
        mercenaryUser.setTeam(team);
        mercenaryUser.setStatus(status);
        return mercenaryUser;
    }
}

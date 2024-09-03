package goinmul.sportsmanage.domain.socialMatch;


import goinmul.sportsmanage.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialUser {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_user_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "social_match_id")
    private SocialMatch socialMatch;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static SocialUser createSocialUser(SocialMatch socialMatch, User user){
        SocialUser socialUser = new SocialUser();
        socialUser.setSocialMatch(socialMatch);
        socialUser.setUser(user);
        return socialUser;
    }

    public static SocialUser createSocialUser(User user){
        SocialUser socialUser = new SocialUser();
        socialUser.setUser(user);
        return socialUser;
    }
    /*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocialUser that = (SocialUser) o;
        return Objects.equals(getSocialMatch(), that.getSocialMatch()) && Objects.equals(getUser().getId(), that.getUser().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSocialMatch(), getUser());
    }
     */

}

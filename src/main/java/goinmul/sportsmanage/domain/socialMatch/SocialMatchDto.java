package goinmul.sportsmanage.domain.socialMatch;

import com.fasterxml.jackson.annotation.JsonInclude;
import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.Reservation;
import goinmul.sportsmanage.domain.Sports;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.socialMatch.SocialMatch;
import goinmul.sportsmanage.domain.socialMatch.SocialUser;
import goinmul.sportsmanage.domain.teamMatch.TeamMatch;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SocialMatchDto {

    private Long id;

    private Integer maxSize;

    private Gender gender;

    private Reservation reservation;

    private Sports sports;

    //private List<SocialUser> socialUsers = new ArrayList<>(); 유저 PK ID만 필요함
    private List<Long> userIdList = new ArrayList<>();

    private Boolean containsSessionUser;

    //소셜 매치 조회
    public SocialMatchDto(SocialMatch socialMatch, User sessionUser) {
        id = socialMatch.getId();
        maxSize = socialMatch.getMaxSize();
        gender = socialMatch.getGender();
        reservation = socialMatch.getReservation();
        sports = socialMatch.getSports();
        userIdList.addAll(socialMatch.getSocialUsers().stream().map(s -> s.getUser().getId()).collect(Collectors.toList()));

        containsSessionUser = sessionUser != null && userIdList.contains(sessionUser.getId());
        //socialUsers = socialMatch.getSocialUsers();
    }

    //소셜 매치 신청 취소
    public SocialMatchDto(SocialMatch socialMatch) {
        id = socialMatch.getId();
        maxSize = socialMatch.getMaxSize();
        gender = socialMatch.getGender();
        reservation = socialMatch.getReservation();
        sports = socialMatch.getSports();
        userIdList.addAll(socialMatch.getSocialUsers().stream().map(s -> s.getUser().getId()).collect(Collectors.toList()));
        //socialUsers = socialMatch.getSocialUsers();
    }


}

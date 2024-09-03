package goinmul.sportsmanage.domain.teamMatch;



import com.fasterxml.jackson.annotation.JsonInclude;
import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.Reservation;
import goinmul.sportsmanage.domain.Sports;
import goinmul.sportsmanage.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamMatchDto {

    private Long id;

    private Integer maxSize; //nowsize는 userIdList 갯수 또는 SocialUser 개수로 대체함

    private Gender gender;

    private Reservation reservation;

    private Sports sports;

    private List<String> teams = new ArrayList<>();

    private List<Long> userIdList = new ArrayList<>();

    private Boolean containsSessionUser;
    private Boolean containsSessionUserTeam;

    //팀 매치 조회
    public TeamMatchDto(TeamMatch teamMatch, User sessionUser) {
        id = teamMatch.getId();
        maxSize = teamMatch.getMaxSize();
        gender = teamMatch.getGender();
        reservation = teamMatch.getReservation();
        sports = teamMatch.getSports();
        userIdList.addAll(teamMatch.getTeamUsers().stream().map(t -> t.getUser().getId()).collect(Collectors.toList()));

        //중복 될 수 있어서 set으로 했습니다.
        if(!teamMatch.getTeamUsers().get(0).getTeam().getClass().getName().contains("Proxy")){
            teams.addAll(teamMatch.getTeamUsers().stream().map(t -> t.getTeam().getName()).collect(Collectors.toSet()));
        }

        containsSessionUser = sessionUser != null && userIdList.contains(sessionUser.getId());
        containsSessionUserTeam = sessionUser != null && teams.contains(sessionUser.getTeam().getName());
    }

    public TeamMatchDto(TeamMatch teamMatch) {
        id = teamMatch.getId();
        maxSize = teamMatch.getMaxSize();
        gender = teamMatch.getGender();
        reservation = teamMatch.getReservation();
        sports = teamMatch.getSports();
        userIdList.addAll(teamMatch.getTeamUsers().stream().map(t -> t.getUser().getId()).collect(Collectors.toList()));
        //중복 될 수 있어서 set으로 했습니다.
        if(!teamMatch.getTeamUsers().get(0).getTeam().getClass().getName().contains("Proxy")){
            teams.addAll(teamMatch.getTeamUsers().stream().map(t -> t.getTeam().getName()).collect(Collectors.toSet()));
        }
    }
}

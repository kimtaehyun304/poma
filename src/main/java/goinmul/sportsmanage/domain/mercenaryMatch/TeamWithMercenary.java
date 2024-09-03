package goinmul.sportsmanage.domain.mercenaryMatch;


import com.fasterxml.jackson.annotation.JsonInclude;
import goinmul.sportsmanage.domain.Team;
import goinmul.sportsmanage.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class TeamWithMercenary {

    //private Team team;
    private Long id;
    private String name;
    private Integer mercenariesMaxSize;
    private List<Long> originalMemberIdList = new ArrayList<>();
    private List<Long> mercenaryIdList = new ArrayList<>();
    private Boolean containsSessionUserAsMercenary;
    private Boolean isEqualToSessionTeam;

    //용병 매치 조회
    public TeamWithMercenary(Team team, Integer mercenariesMaxSize, List<Long> originalMemberIdList, List<Long> mercenaryIdList, User sessionUser) {
        this.id = team.getId();
        this.name = team.getName();
        this.mercenariesMaxSize = mercenariesMaxSize;
        this.originalMemberIdList.addAll(originalMemberIdList);
        this.mercenaryIdList.addAll(mercenaryIdList);
        containsSessionUserAsMercenary = sessionUser != null && mercenaryIdList.contains(sessionUser.getId());
        isEqualToSessionTeam = sessionUser != null && sessionUser.getTeam().getName().equals(name);
    }

    public TeamWithMercenary(Team team, Integer mercenariesMaxSize, List<Long> originalMemberIdList, List<Long> mercenaryIdList) {
        this.id = team.getId();
        this.name = team.getName();
        this.mercenariesMaxSize = mercenariesMaxSize;
        this.originalMemberIdList.addAll(originalMemberIdList);
        this.mercenaryIdList.addAll(mercenaryIdList);
    }

    /*
    public TeamWithMercenary(Team team, Integer mercenariesMaxSize, List<Long> mercenaryIdList) {
        this.team = team;
        this.mercenariesMaxSize = mercenariesMaxSize;
        this.mercenaryIdList = mercenaryIdList;
    }
     */


}

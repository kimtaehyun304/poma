package goinmul.sportsmanage.domain.mercenaryMatch;


import com.fasterxml.jackson.annotation.JsonInclude;
import goinmul.sportsmanage.domain.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MercenaryMatchDto {

    private Long id;

    private Integer maxSize;

    private Gender gender;

    //선발 멤버 선택에선 안필요함 -> 계층 줄이기
    private Reservation reservation;

    private Sports sports;

    private List<Long> userIdList = new ArrayList<>();

    private List<TeamWithMercenary> teams = new ArrayList<>();

    //private Boolean containsSessionUser;
    private Boolean containsSessionUserAsOriginalMember;
    private Boolean containsSessionUserTeam;

    // 용병 매치 조회
    public MercenaryMatchDto(MercenaryMatch mercenaryMatch, User sessionUser) {
        id = mercenaryMatch.getId();
        maxSize = mercenaryMatch.getMaxSize();
        gender = mercenaryMatch.getGender();
        reservation = mercenaryMatch.getReservation();
        sports = mercenaryMatch.getSports();
        userIdList.addAll(mercenaryMatch.getMercenaryMatchUsers().stream().map(u -> u.getUser().getId()).toList());

        Set<Team> teamsSet = new HashSet<>();
        for (MercenaryMatchUser u : mercenaryMatch.getMercenaryMatchUsers()) {
            if(u.getStatus().equals(UserStatus.TEAM))
                teamsSet.add(u.getTeam());
            if (teamsSet.size() == 2) break;
        }

        List<Team> teams = new ArrayList<>(teamsSet);
        for (Team team : teams) {
            List<Long> mercenaryIdList = mercenaryMatch.getMercenaryMatchUsers().stream().filter(u -> u.getTeam().equals(team)).filter(u -> u.getStatus().equals(UserStatus.MERCENARY)).map(u -> u.getUser().getId()).toList();
            List<Long> originalMemberIdList = mercenaryMatch.getMercenaryMatchUsers().stream().filter(u -> u.getTeam().equals(team)).filter(u -> u.getStatus().equals(UserStatus.TEAM)).map(u -> u.getUser().getId()).toList();
            TeamWithMercenary teamWithMercenary = new TeamWithMercenary(team, mercenaryMatch.getMaxSize()/2-originalMemberIdList.size(), originalMemberIdList, mercenaryIdList, sessionUser);
            this.teams.add(teamWithMercenary);
        }

        List<Long> originalMembers = new ArrayList<>();
        for (TeamWithMercenary team : this.teams)
            originalMembers.addAll(team.getOriginalMemberIdList());

        containsSessionUserAsOriginalMember = sessionUser != null && originalMembers.contains(sessionUser.getId());

        List<Long> teamIdList = teams.stream().map(t -> t.getId()).toList();
        containsSessionUserTeam = sessionUser != null && teamIdList.contains(sessionUser.getTeam().getId());


        /*
        List<Long> AllMercenaryIdList = new ArrayList<>();

        for (TeamWithMercenary team : this.teams)
            AllMercenaryIdList.addAll(team.getMercenaryIdList());
        */

        //containsSessionUser = sessionUser != null && userIdList.contains(sessionUser.getId()) && !AllMercenaryIdList.contains(sessionUser.getId());
    }

    public MercenaryMatchDto(MercenaryMatch mercenaryMatch) {
        id = mercenaryMatch.getId();
        maxSize = mercenaryMatch.getMaxSize();
        gender = mercenaryMatch.getGender();
        reservation = mercenaryMatch.getReservation();
        sports = mercenaryMatch.getSports();
        userIdList.addAll(mercenaryMatch.getMercenaryMatchUsers().stream().map(u -> u.getUser().getId()).toList());

        Set<Team> teamsSet = new HashSet<>();
        for (MercenaryMatchUser u : mercenaryMatch.getMercenaryMatchUsers()) {
            if(u.getStatus().equals(UserStatus.TEAM))
                teamsSet.add(u.getTeam());
            if (teamsSet.size() == 2) break;
        }

        List<Team> teams = new ArrayList<>(teamsSet);
        for (Team team : teams) {
            List<Long> mercenaryIdList = mercenaryMatch.getMercenaryMatchUsers().stream().filter(u -> u.getTeam().equals(team)).filter(u -> u.getStatus().equals(UserStatus.MERCENARY)).map(u -> u.getUser().getId()).toList();
            List<Long> originalMemberIdList = mercenaryMatch.getMercenaryMatchUsers().stream().filter(u -> u.getTeam().equals(team)).filter(u -> u.getStatus().equals(UserStatus.TEAM)).map(u -> u.getUser().getId()).toList();
            TeamWithMercenary teamWithMercenary = new TeamWithMercenary(team, mercenaryMatch.getMaxSize()/2-originalMemberIdList.size(), originalMemberIdList, mercenaryIdList);
            this.teams.add(teamWithMercenary);
        }
    }

}

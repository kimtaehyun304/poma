package goinmul.sportsmanage.domain.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import goinmul.sportsmanage.domain.Authority;
import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.Team;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Long id;
    private String loginId;

    @JsonIgnore
    private String password;

    private String name;

    @JsonIgnore
    private String phone;

    private LocalDateTime regdate;

    private Gender gender;

    private Team team;

    @JsonIgnore
    private String email;

    private Authority authority;

    private Integer money;

    public UserDto(User user){
        id = user.getId();
        name = user.getName();
    }



}

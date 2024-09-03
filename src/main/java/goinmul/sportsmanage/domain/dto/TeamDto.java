package goinmul.sportsmanage.domain.dto;

import goinmul.sportsmanage.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamDto {

    private Long id;

    private String name;

    private LocalDateTime createdAt;

    private List<User> users = new ArrayList<>();

}

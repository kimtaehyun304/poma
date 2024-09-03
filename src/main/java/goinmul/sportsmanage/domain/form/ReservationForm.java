package goinmul.sportsmanage.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.Match;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ReservationForm {

    @NotNull
    private Long groundId;

    @NotNull
    @FutureOrPresent(message = "지난 날은 예약할 수 없습니다")
    private LocalDate date;

    @NotNull
    private int time;

    @NotNull
    @JsonDeserialize(using = MatchStringToEnumDeserializer.class)
    private Match match;

    @NotNull
    @JsonDeserialize(using = GenderStringToEnumDeserializer.class)
    private Gender gender;

    @NotNull
    private int maxSize;

    private List<Long> userIdList = new ArrayList<>();

    //private Integer mercenarySize;

}
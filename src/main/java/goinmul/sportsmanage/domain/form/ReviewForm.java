package goinmul.sportsmanage.domain.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
public class ReviewForm {

    @NotNull
    Long mentorId;
    @NotNull
    Integer score;
    @NotEmpty
    String description;
    @NotEmpty
    String  sports;

}

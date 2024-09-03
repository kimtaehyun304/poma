package goinmul.sportsmanage.domain.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class CommentsForm {

    @NotEmpty
    @Length(max = 500)
    String content;
    Long parentId;

}

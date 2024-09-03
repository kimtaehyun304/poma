package goinmul.sportsmanage.domain.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class CustomerPostForm {

    @NotEmpty
    @Size(max = 50)
    private String title;

    @NotEmpty
    @Size(max = 1000)
    private String content;

    private boolean secret;

}

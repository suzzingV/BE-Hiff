package hiff.hiff.behiff.domain.user.presentation.dto.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PhoneNumRequest {

    @Size(min = 1, max = 20)
    @NotEmpty
    @Pattern(regexp = "^[0-9]*$")
    private String phoneNum;
}

package hiff.hiff.behiff.domain.user.presentation.dto.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BirthRequest {

    @NotEmpty
    @Min(1900)
    @Max(2024)
    private Integer birthYear;

    @NotEmpty
    @Min(1)
    @Max(12)
    private Integer birthMonth;

    @NotEmpty
    @Min(1)
    @Max(31)
    private Integer birthDay;
}

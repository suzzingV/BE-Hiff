package hiff.hiff.behiff.domain.user.presentation.dto.req;

import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import hiff.hiff.behiff.global.validation.annotation.ValidGender;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class GenderRequest {

    @ValidGender
    @NotNull
    private Gender gender;
}

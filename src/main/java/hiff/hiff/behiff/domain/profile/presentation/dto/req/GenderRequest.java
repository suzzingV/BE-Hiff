package hiff.hiff.behiff.domain.profile.presentation.dto.req;

import hiff.hiff.behiff.domain.profile.domain.enums.Gender;
import hiff.hiff.behiff.global.validation.annotation.ValidGender;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class GenderRequest {

    @ValidGender
    @NotNull
    private Gender gender;
}

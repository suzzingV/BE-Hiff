package hiff.hiff.behiff.domain.user.presentation.dto.req;

import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import hiff.hiff.behiff.global.validation.annotation.ValidGender;
import lombok.Getter;

@Getter
public class GenderRequest {

    @ValidGender
    private Gender gender;
}

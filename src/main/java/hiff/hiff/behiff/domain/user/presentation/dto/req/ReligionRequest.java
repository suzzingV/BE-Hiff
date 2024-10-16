package hiff.hiff.behiff.domain.user.presentation.dto.req;

import hiff.hiff.behiff.domain.user.domain.enums.Religion;
import hiff.hiff.behiff.global.validation.annotation.ValidReligion;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReligionRequest {

    @ValidReligion
    @NotNull
    private Religion religion;
}

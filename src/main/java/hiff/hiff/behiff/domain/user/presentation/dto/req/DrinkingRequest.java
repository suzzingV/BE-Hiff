package hiff.hiff.behiff.domain.user.presentation.dto.req;

import hiff.hiff.behiff.domain.user.domain.enums.Drinking;
import hiff.hiff.behiff.global.validation.annotation.ValidDrinking;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DrinkingRequest {

    @ValidDrinking
    @NotNull
    Drinking isDrinking;
}

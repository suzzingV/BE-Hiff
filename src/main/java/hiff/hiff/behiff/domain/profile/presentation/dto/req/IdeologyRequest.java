package hiff.hiff.behiff.domain.profile.presentation.dto.req;

import hiff.hiff.behiff.domain.profile.domain.enums.Ideology;
import hiff.hiff.behiff.global.validation.annotation.ValidIdeology;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class IdeologyRequest {

    @ValidIdeology
    @NotNull
    private Ideology ideology;
}

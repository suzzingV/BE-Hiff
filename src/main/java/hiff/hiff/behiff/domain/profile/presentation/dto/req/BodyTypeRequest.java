package hiff.hiff.behiff.domain.profile.presentation.dto.req;

import hiff.hiff.behiff.domain.profile.domain.enums.BodyType;
import hiff.hiff.behiff.global.validation.annotation.ValidBodyType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BodyTypeRequest {

    @ValidBodyType
    @NotNull
    private BodyType bodyType;
}

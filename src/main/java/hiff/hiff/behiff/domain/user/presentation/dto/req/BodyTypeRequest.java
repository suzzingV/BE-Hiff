package hiff.hiff.behiff.domain.user.presentation.dto.req;

import hiff.hiff.behiff.domain.user.domain.enums.BodyType;
import hiff.hiff.behiff.global.validation.annotation.ValidBodyType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BodyTypeRequest {

    @ValidBodyType
    @NotNull
    private BodyType bodyType;
}

package hiff.hiff.behiff.domain.matching.presentation.dto.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MatchingRequest {

    @NotNull
    private String posX;

    @NotNull
    private String posY;
}

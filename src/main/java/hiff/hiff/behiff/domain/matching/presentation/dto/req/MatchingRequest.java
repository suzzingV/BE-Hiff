package hiff.hiff.behiff.domain.matching.presentation.dto.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class MatchingRequest {

    @NotEmpty
    private String posX;

    @NotEmpty
    private String posY;
}

package hiff.hiff.behiff.domain.user.presentation.dto.req;

import hiff.hiff.behiff.domain.user.domain.enums.Fashion;
import hiff.hiff.behiff.global.validation.annotation.ValidFashion;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;

@Getter
public class FashionRequest {

    @NotEmpty
    private List<@ValidFashion Fashion> fashions;
}

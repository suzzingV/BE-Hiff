package hiff.hiff.behiff.domain.profile.presentation.dto.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class IntroductionUpdateRequest {

    @NotNull
    private Long questionId;

    @Size(min = 50, max = 100)
    @NotEmpty
    private String content;
}

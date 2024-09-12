package hiff.hiff.behiff.domain.user.presentation.dto.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class NicknameRequest {

    @Size(min = 1, max = 8)
    @NotEmpty
    private String nickname;
}

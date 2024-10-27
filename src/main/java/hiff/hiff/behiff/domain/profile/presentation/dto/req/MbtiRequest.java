package hiff.hiff.behiff.domain.profile.presentation.dto.req;

import hiff.hiff.behiff.domain.profile.domain.enums.Mbti;
import hiff.hiff.behiff.global.validation.annotation.ValidMbti;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MbtiRequest {

    @ValidMbti
    @NotNull
    private Mbti mbti;
}

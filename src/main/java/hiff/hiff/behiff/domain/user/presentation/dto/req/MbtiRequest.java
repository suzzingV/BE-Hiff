package hiff.hiff.behiff.domain.user.presentation.dto.req;

import hiff.hiff.behiff.domain.user.domain.enums.Mbti;
import hiff.hiff.behiff.global.validation.annotation.ValidMbti;
import lombok.Getter;

@Getter
public class MbtiRequest {

    @ValidMbti
    private Mbti mbti;
}

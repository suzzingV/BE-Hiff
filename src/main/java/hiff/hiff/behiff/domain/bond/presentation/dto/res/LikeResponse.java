package hiff.hiff.behiff.domain.bond.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LikeResponse {

    private Long senderId;

    private Long responderId;

    public static LikeResponse of(Long senderId, Long responderId) {
        return LikeResponse.builder()
                .senderId(senderId)
                .responderId(responderId)
                .build();
    }
}

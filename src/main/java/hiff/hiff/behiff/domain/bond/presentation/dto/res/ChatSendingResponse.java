package hiff.hiff.behiff.domain.bond.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatSendingResponse {

    private Long responderId;

    public static ChatSendingResponse from(Long responderId) {
        return ChatSendingResponse.builder()
                .responderId(responderId)
                .build();
    }
}

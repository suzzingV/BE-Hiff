package hiff.hiff.behiff.domain.bond.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatAcceptanceResponse {

    private Long senderId;

    public static ChatAcceptanceResponse from(Long senderId) {
         return ChatAcceptanceResponse.builder()
                .senderId(senderId)
                .build();
    }
}

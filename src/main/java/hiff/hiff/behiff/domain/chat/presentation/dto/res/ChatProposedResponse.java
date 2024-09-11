package hiff.hiff.behiff.domain.chat.presentation.dto.res;

import lombok.Builder;

@Builder
public class ChatProposedResponse {

    private String proposerNickname;

    private Boolean isAccepted;
}

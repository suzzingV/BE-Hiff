package hiff.hiff.behiff.domain.chat.presentation.dto.res;

import lombok.Builder;

@Builder
public class ChatProposerResponse {

    private String proposerNickname;

    private Boolean isAccepted;
}

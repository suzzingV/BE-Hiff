package hiff.hiff.behiff.domain.bond.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatProposerResponse {

    private Long proposerId;

    private String proposerNickname;

    private Boolean isAccepted;
}

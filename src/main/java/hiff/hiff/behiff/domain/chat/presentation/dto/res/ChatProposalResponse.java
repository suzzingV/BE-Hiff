package hiff.hiff.behiff.domain.chat.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Builder
public class ChatProposalResponse {

    private Long proposerId;

    private Long proposedId;
}

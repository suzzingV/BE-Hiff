package hiff.hiff.behiff.domain.chat.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatProposalResponse {

    private Long proposerId;

    private Long proposedId;
}

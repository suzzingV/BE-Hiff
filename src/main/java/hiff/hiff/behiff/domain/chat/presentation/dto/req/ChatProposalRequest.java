package hiff.hiff.behiff.domain.chat.presentation.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChatProposalRequest {

    @NotNull
    private Long matchedId;
}

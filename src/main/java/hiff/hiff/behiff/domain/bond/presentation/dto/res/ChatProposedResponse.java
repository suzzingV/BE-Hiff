package hiff.hiff.behiff.domain.bond.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatProposedResponse {

    private Long proposedId;

    private String proposedNickname;

    private Boolean isAccepted;
}

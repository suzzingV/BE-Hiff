package hiff.hiff.behiff.domain.chat.presentation.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChatAcceptanceRequest {

    @NotNull
    private Long proposerId;
}

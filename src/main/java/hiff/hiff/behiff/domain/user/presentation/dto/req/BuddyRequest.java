package hiff.hiff.behiff.domain.user.presentation.dto.req;

import hiff.hiff.behiff.domain.user.domain.enums.Buddy;
import hiff.hiff.behiff.global.validation.annotation.ValidBuddy;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BuddyRequest {

    @ValidBuddy
    @NotNull
    private Buddy buddy;
}

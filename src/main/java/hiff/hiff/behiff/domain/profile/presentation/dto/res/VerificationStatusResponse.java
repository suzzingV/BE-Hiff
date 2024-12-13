package hiff.hiff.behiff.domain.profile.presentation.dto.res;

import hiff.hiff.behiff.domain.profile.domain.enums.VerificationStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VerificationStatusResponse {

    private VerificationStatus status;

    public static VerificationStatusResponse from(VerificationStatus status) {
        return VerificationStatusResponse.builder()
            .status(status)
            .build();
    }
}

package hiff.hiff.behiff.domain.profile.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileUpdateResponse {

    private Long userId;

    public static ProfileUpdateResponse from(Long userId) {
        return ProfileUpdateResponse.builder()
            .userId(userId)
            .build();
    }
}

package hiff.hiff.behiff.domain.user.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserUpdateResponse {

    private Long userId;

    public static UserUpdateResponse from(Long userId) {
        return UserUpdateResponse.builder()
                .userId(userId)
                .build();
    }
}

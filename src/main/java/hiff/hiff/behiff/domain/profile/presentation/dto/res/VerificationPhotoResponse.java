package hiff.hiff.behiff.domain.profile.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VerificationPhotoResponse {

    private String photoUrl;

    public static VerificationPhotoResponse from(String photoUrl) {
        return VerificationPhotoResponse.builder()
            .photoUrl(photoUrl)
            .build();
    }
}

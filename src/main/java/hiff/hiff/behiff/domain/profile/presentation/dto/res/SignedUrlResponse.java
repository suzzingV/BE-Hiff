package hiff.hiff.behiff.domain.profile.presentation.dto.res;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignedUrlResponse {

    private String mainSignedUrl;

    private List<String> signedUrls;
}

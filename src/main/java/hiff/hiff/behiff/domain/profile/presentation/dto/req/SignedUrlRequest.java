package hiff.hiff.behiff.domain.profile.presentation.dto.req;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;

@Getter
public class SignedUrlRequest {

    @NotEmpty
    private String mainPhotoName;

    @NotEmpty
    private List<String> photoNames;
}

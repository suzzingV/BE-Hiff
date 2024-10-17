package hiff.hiff.behiff.domain.user.presentation.dto.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;

@Getter
public class SignedUrlRequest {

    @NotEmpty
    private String mainPhotoName;

    @NotEmpty
    private List<String> photoNames;
}

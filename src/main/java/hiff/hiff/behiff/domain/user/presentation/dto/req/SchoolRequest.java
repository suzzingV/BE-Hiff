package hiff.hiff.behiff.domain.user.presentation.dto.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class SchoolRequest {

    @NotEmpty
    private String school;
}

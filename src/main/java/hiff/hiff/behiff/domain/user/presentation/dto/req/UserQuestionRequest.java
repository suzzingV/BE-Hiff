package hiff.hiff.behiff.domain.user.presentation.dto.req;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;

@Getter
public class UserQuestionRequest {

    @NotEmpty
    private List<Long> questionIds;
}

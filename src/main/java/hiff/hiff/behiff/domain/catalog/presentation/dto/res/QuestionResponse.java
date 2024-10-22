package hiff.hiff.behiff.domain.catalog.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuestionResponse {

    private Long id;

    private String question;
}

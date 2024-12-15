package hiff.hiff.behiff.domain.profile.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserIntroductionDto {

    private Long questionId;

    private String question;

    private String content;
}

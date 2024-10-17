package hiff.hiff.behiff.domain.user.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserIntroductionDto {

    private String question;

    private String content;
}

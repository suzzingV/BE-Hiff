package hiff.hiff.behiff.domain.matching.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MatchingSimpleResponse {

    private Long userId;

    private String nickname;

    private Integer age;

    private String location;

    private String mainPhoto;
}

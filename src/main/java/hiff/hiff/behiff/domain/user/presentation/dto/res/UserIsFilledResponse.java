package hiff.hiff.behiff.domain.user.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserIsFilledResponse {

    private Long userId;

    private Boolean isFilled;


}

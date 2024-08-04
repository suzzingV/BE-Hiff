package hiff.hiff.behiff.domain.user.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HobbyResponse {

    private Long id;
    private String name;
}

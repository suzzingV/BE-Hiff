package hiff.hiff.behiff.domain.user.presentation.dto.req;

import java.util.List;
import lombok.Getter;

@Getter
public class LifeStyleRequest {

    private List<Long> originLifeStyles;

    private List<String> newLifeStyles;
}
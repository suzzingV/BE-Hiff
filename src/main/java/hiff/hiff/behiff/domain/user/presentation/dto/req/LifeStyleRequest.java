package hiff.hiff.behiff.domain.user.presentation.dto.req;

import lombok.Getter;

import java.util.List;

@Getter
public class LifeStyleRequest {

    private List<Long> originLifeStyles;

    private List<String> newLifeStyles;
}
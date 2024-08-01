package hiff.hiff.behiff.domain.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum Income {

    FROM_0_TO_200("0 ~ 200만원"),
    FROM_200_TO_300("200 ~ 300만원"),
    FROM_300_TO_400("300 ~ 400만원"),
    FROM_400_TO_500("400 ~ 500만원"),
    FROM_500("500만원 이상");

    private final String key;

    public static final Set<Income> ALL_VALUES = EnumSet.allOf(Income.class);
}

package hiff.hiff.behiff.domain.profile.domain.enums;

import java.util.EnumSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Buddy {
    ZERO("없음"),
    ONE_TO_TWO("1~2명"),
    THREE_TO_FIVE("3~4명"),
    OVER_FIVE("5명 이상");

    private final String text;

    public static final Set<Buddy> ALL_VALUES = EnumSet.allOf(Buddy.class);
}

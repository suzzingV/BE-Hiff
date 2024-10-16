package hiff.hiff.behiff.domain.user.domain.enums;

import java.util.EnumSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Fashion {

    CASUAL("캐주얼"),
    STREET("스트릿"),
    FORMAL("포멀"),
    FRIEND_LOOK("남친/여친룩"),
    SPORTY("스포티");

    private final String text;

    public static final Set<Fashion> ALL_VALUES = EnumSet.allOf(Fashion.class);
}

package hiff.hiff.behiff.domain.user.domain.enums;

import java.util.EnumSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Religion {

    NONE("없음"),
    CHRISTIANITY("기독교"),
    BUDDHISM("불교"),
    CATHOLIC("천주교"),
    OTHER("그외");

    private final String text;

    public static final Set<Religion> ALL_VALUES = EnumSet.allOf(Religion.class);
}

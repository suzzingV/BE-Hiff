package hiff.hiff.behiff.domain.user.domain.enums;

import java.util.EnumSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BodyType {

    MUSCULAR("근육질"),
    VOLUME("볼륨"),
    NORMAL("보통"),
    PLUMP("통통한"),
    SLIM("슬림");

    private final String text;

    public static final Set<BodyType> ALL_VALUES = EnumSet.allOf(BodyType.class);
}

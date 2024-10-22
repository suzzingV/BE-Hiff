package hiff.hiff.behiff.domain.user.domain.enums;

import java.util.EnumSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Ideology {

    CONSERVATIVE("보수"),
    LIBERAL("진보"),
    MODERATE("중도"),
    NONE("관심없음");

    private final String text;

    public static final Set<Ideology> ALL_VALUES = EnumSet.allOf(Ideology.class);
}

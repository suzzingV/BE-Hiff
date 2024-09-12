package hiff.hiff.behiff.domain.user.domain.enums;

import java.util.EnumSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {

    MALE("남"), FEMALE("여");

    private final String key;

    public static final Set<Gender> ALL_VALUES = EnumSet.allOf(Gender.class);
}

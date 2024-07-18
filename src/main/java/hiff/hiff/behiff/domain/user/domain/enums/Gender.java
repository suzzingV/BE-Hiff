package hiff.hiff.behiff.domain.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum Gender {

    MALE("남"), FEMALE("여"), ETC("기타");

    private final String key;

    public static final Set<Gender> ALL_VALUES = EnumSet.allOf(Gender.class);
}

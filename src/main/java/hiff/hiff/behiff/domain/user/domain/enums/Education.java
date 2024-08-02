package hiff.hiff.behiff.domain.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum Education {

    PRIVATE("비공개"),
    HIGH_SCHOOL_GRADUATE("고등학교 졸업"),
    BEING_IN_UNIVERSITY("대학교 재학"),
    UNIVERSITY_GRADUATION("대학교 졸업"),
    MASTER_DEGREE("석사"),
    DOCTOR_DEGREE("박사");

    private final String key;

    public static final Set<Education> ALL_VALUES = EnumSet.allOf(Education.class);
}

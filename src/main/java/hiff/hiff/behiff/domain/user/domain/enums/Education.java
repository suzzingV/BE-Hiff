package hiff.hiff.behiff.domain.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Education {

    HIGH_SCHOOL_GRADUATE("고등학교 졸업"),
    BEING_IN_UNIVERSITY("대학교 재학"),
    UNIVERSITY_GRADUATION("대학교 졸업"),
    MASTER_DEGREE("석사"),
    DOCTOR_DEGREE("박사");

    private final String key;
}

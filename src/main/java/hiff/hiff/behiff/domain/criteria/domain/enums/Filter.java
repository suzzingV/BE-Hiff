package hiff.hiff.behiff.domain.criteria.domain.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

@Getter
public enum Filter {
    HEIGHT("키", 3),
    BODY_TYPE("체형", 7),
    SMOKING("흡연", 1),
    AGE("나이", 1),
    EDUCATION("학력", 11),
    INCOME("소득", 11),
    APPEARANCE("외모", 8);

    private final Integer point;
    private final String name;
    public static final Set<Filter> ALL_VALUES = Collections.unmodifiableSet(EnumSet.allOf(Filter.class));

    Filter(String name, Integer point) {
        this.point = point;
        this.name = name;
    }
}

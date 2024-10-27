package hiff.hiff.behiff.domain.profile.domain.enums;

import java.util.EnumSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Company {
    BIG("대기업"),
    MEDIUM("중견기업"),
    SMALL("중소기업"),
    START_UP("스타트업"),
    PUBLIC("공무원"),
    FOREIGN("외국계 회사");

    private final String text;

    public static final Set<Company> ALL_VALUES = EnumSet.allOf(Company.class);
}

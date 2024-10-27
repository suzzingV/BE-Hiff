package hiff.hiff.behiff.domain.profile.domain.enums;

import java.util.EnumSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Drinking {
    EVERYDAY("매일"),
    WEEK_3_TO_4("주 3~4회"),
    WEEK_1_TO_2("주 1~2회"),
    SPECIAL_DAY("특별한 날"),
    NON_DRINKING("안 마심");

    private final String text;

    public static final Set<Drinking> ALL_VALUES = EnumSet.allOf(Drinking.class);
}

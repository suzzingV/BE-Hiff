package hiff.hiff.behiff.domain.profile.domain.enums;

import java.util.EnumSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContactFrequency {

    ONCE_A_DAY("하루 1회"),
    ONCE_EVERY_6_HOURS("6시간에 1회"),
    ONCE_EVERY_HOURS("1시간에 1회"),
    WHENEVER("틈날 때마다"),
    ALWAYS_EXCEPT_WORKING("일할 때 빼고 계속");

    private final String text;

    public static final Set<ContactFrequency> ALL_VALUES = EnumSet.allOf(ContactFrequency.class);
}

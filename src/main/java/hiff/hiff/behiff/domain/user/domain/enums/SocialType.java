package hiff.hiff.behiff.domain.user.domain.enums;

import java.util.EnumSet;
import java.util.Set;

public enum SocialType {
    GOOGLE, KAKAO, NAVER;

    public static final Set<SocialType> ALL_VALUES = EnumSet.allOf(SocialType.class);
}

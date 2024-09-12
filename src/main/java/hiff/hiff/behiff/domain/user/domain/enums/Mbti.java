package hiff.hiff.behiff.domain.user.domain.enums;

import java.util.EnumSet;
import java.util.Set;

public enum Mbti {

    INTP, INTJ, INFP, INFJ,
    ISTP, ISTJ, ISFP, ISFJ,
    ENTP, ENTJ, ENFP, ENFJ,
    ESTP, ESTJ, ESFP, ESFJ;

    public static final Set<Mbti> ALL_VALUES = EnumSet.allOf(Mbti.class);
}

package hiff.hiff.behiff.domain.user.domain.enums;

import java.util.EnumSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConflictResolution {

    THINKING_ALONE("혼자 생각할 시간이 필요함"),
    TALK_IMMEDIATELY("즉시 대화로 해결해야 함"),
    UNCERTAINTY("잘 모르겠음");

    private final String text;

    public static final Set<ConflictResolution> ALL_VALUES = EnumSet.allOf(ConflictResolution.class);
}

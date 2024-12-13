package hiff.hiff.behiff.domain.profile.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VerificationStatus {

    FAILED("인증 실패"),
    COMPLETED("인증 완료"),
    IN_PROGRESS("인증 처리 중");

    private final String text;
}

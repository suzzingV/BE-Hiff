package hiff.hiff.behiff.global.response.properties;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400
    REFRESH_TOKEN_REQUIRED(BAD_REQUEST, "refresh token이 필요합니다."),
    EMAIL_NOT_EXTRACTED(BAD_REQUEST, "이메일을 추출할 수 없습니다."),
    S3_ACCESS_DENIED(BAD_REQUEST, "s3에 접근할 수 없습니다."),
    PHOTO_QUANTITY_ERROR(BAD_REQUEST, "사진은 2개 이상 등록해야 합니다."),
    VALIDATION_ERROR(BAD_REQUEST, "입력값이 유효하지 않습니다."),
    NICKNAME_ALREADY_EXISTS(BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    HOBBY_ALREADY_EXISTS(BAD_REQUEST, "이미 존재하는 취미입니다."),
    BELIEF_ALREADY_EXISTS(BAD_REQUEST, "이미 존재하는 가치관입니다."),
    DISTANCE_RANGE_REVERSE(BAD_REQUEST, "거리의 최대값보다 최소값이 클 수 없습니다."),

    // 401
    SECURITY_UNAUTHORIZED(UNAUTHORIZED, "인증 정보가 유효하지 않습니다"),
    SECURITY_INVALID_TOKEN(UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    SECURITY_INVALID_REFRESH_TOKEN(UNAUTHORIZED, "refresh token이 유효하지 않습니다."),
    SECURITY_INVALID_ACCESS_TOKEN(UNAUTHORIZED, "access token이 유효하지 않습니다."),

    // 403
    SECURITY_ACCESS_DENIED(FORBIDDEN, "접근 권한이 없습니다."),

    // 404
    USER_NOT_FOUND(NOT_FOUND, "user을 찾을 수 없습니다."),
    JOB_NOT_FOUND(NOT_FOUND, "직업을 찾을 수 없습니다."),
    HOBBY_NOT_FOUND(NOT_FOUND, "취미를 찾을 수 없습니다."),
    BELIEF_NOT_FOUND(NOT_FOUND, "가치관을 찾을 수 없습니다."),

    // 500
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "예상치 못한 서버 에러가 발생하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

package hiff.hiff.behiff.global.response.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400
    REFRESH_TOKEN_REQUIRED(BAD_REQUEST, "refresh token이 필요합니다."),
    ACCESS_TOKEN_REQUIRED(BAD_REQUEST, "access token이 필요합니다."),
    EMAIL_NOT_EXTRACTED(BAD_REQUEST, "이메일을 추출할 수 없습니다."),
    S3_ACCESS_DENIED(BAD_REQUEST, "s3에 접근할 수 없습니다."),
    PHOTO_QUANTITY_ERROR(BAD_REQUEST, "사진은 2개 이상 등록해야 합니다."),
    VALIDATION_ERROR(BAD_REQUEST, "입력값이 유효하지 않습니다."),
    NICKNAME_ALREADY_EXISTS(BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    HOBBY_ALREADY_EXISTS(BAD_REQUEST, "이미 존재하는 취미입니다."),
    LIFESTYLE_ALREADY_EXISTS(BAD_REQUEST, "이미 존재하는 라이프스타일입니다."),
    DISTANCE_RANGE_REVERSE(BAD_REQUEST, "거리의 최대값보다 최소값이 클 수 없습니다."),
    JOB_UPDATE_REQUEST_ERROR(BAD_REQUEST, "job id와 new job name 중 반드시 하나만 값이 존재해야 합니다."),
    EVALUATION_INVALID_GENDER(BAD_REQUEST, "첫인상 평가 대상의 성별이 적절하지 않습니다."),
    EVALUATION_COUNT_EXCEED(BAD_REQUEST, "첫인상 평가 횟수를 초과했습니다."),
    EVALUATION_ALREADY(BAD_REQUEST, "이미 첫인상 평가를 한 대상입니다."),
    EVALUATION_COUNT_NOT_ENOUGH(BAD_REQUEST, "받은 첫인상 평가 횟수가 충분하지 않습니다."),
    LACK_OF_HEART(BAD_REQUEST, "하트가 부족합니다."),
    INVALID_SIMILARITY_TYPE(BAD_REQUEST, "적절한 유사도 타입이 아닙니다."),

    // 401
    SECURITY_UNAUTHORIZED(UNAUTHORIZED, "인증 정보가 유효하지 않습니다"),
    SECURITY_INVALID_TOKEN(UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    SECURITY_INVALID_REFRESH_TOKEN(UNAUTHORIZED, "refresh token이 유효하지 않습니다."),

    // 403
    SECURITY_ACCESS_DENIED(FORBIDDEN, "접근 권한이 없습니다."),

    // 404
    USER_NOT_FOUND(NOT_FOUND, "user을 찾을 수 없습니다."),
    JOB_NOT_FOUND(NOT_FOUND, "직업을 찾을 수 없습니다."),
    HOBBY_NOT_FOUND(NOT_FOUND, "취미를 찾을 수 없습니다."),
    LIFESTYLE_NOT_FOUND(NOT_FOUND, "라이프스타일을 찾을 수 없습니다."),
    WEIGHT_VALUE_NOT_FOUND(NOT_FOUND, "해당 유저의 가중치 정보를 찾을 수 없습니다."),
    USER_JOB_NOT_EXISTS(NOT_FOUND, "user의 직업이 존재하지 않습니다."),
    EVALUATED_USER_NOT_FOUND(NOT_FOUND, "첫인상 평가 대상이 존재하지 않습니다."),
    EVALUATION_NOT_FOUND(NOT_FOUND, "첫인상 평가 대상을 찾을 수 없습니다."),
    USER_POS_NOT_FOUND(NOT_FOUND, "사용자의 위치를 찾을 수 없습니다."),
    MATCHING_HISTORY_NOT_FOUND(NOT_FOUND, "매칭 내역이 존재하지 않습니다."),
    MATCHING_NOT_FOUND(NOT_FOUND, "매칭 상대가 존재하지 않습니다."),
    MATCHING_SCORE_NOT_FOUND(NOT_FOUND, "매칭 점수 내역이 존재하지 않습니다."),

    // 500
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "예상치 못한 서버 에러가 발생하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

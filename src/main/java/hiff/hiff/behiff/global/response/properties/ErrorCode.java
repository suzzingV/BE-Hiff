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
    ACCESS_TOKEN_REQUIRED(BAD_REQUEST, "access token이 필요합니다."),
    S3_ACCESS_DENIED(BAD_REQUEST, "s3에 접근할 수 없습니다."),
    PHOTO_QUANTITY_ERROR(BAD_REQUEST, "사진은 2개 이상 등록해야 합니다."),
    VALIDATION_ERROR(BAD_REQUEST, "입력값이 유효하지 않습니다."),
    NICKNAME_ALREADY_EXISTS(BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    HOBBY_ALREADY_EXISTS(BAD_REQUEST, "이미 존재하는 취미입니다."),
    LIFESTYLE_ALREADY_EXISTS(BAD_REQUEST, "이미 존재하는 라이프스타일입니다."),
    DISTANCE_RANGE_REVERSE(BAD_REQUEST, "거리의 최대값보다 최소값이 클 수 없습니다."),
    CAREER_UPDATE_REQUEST_ERROR(BAD_REQUEST, "career id와 new career name 중 반드시 하나만 값이 존재해야 합니다."),
    EVALUATION_INVALID_GENDER(BAD_REQUEST, "첫인상 평가 대상의 성별이 적절하지 않습니다."),
    EVALUATION_COUNT_EXCEED(BAD_REQUEST, "첫인상 평가 횟수를 초과했습니다."),
    EVALUATION_ALREADY(BAD_REQUEST, "이미 첫인상 평가를 한 대상입니다."),
    LACK_OF_COUPON(BAD_REQUEST, "쿠폰이 부족합니다."),
    INVALID_SIMILARITY_TYPE(BAD_REQUEST, "적절한 유사도 타입이 아닙니다."),
    INCOME_WEIGHT_VALUE_PRIVATE(BAD_REQUEST, "소득이 비공개일 경우 가중치는 0이어야 합니다."),
    VERIFICATION_CODE_INCORRECT(BAD_REQUEST, "인증 코드가 올바르지 않습니다."),
    FCM_SEND_ERROR(BAD_REQUEST, "FCM에 메시지를 보내는 도중 에러가 발생하였습니다."),
    USER_ALREADY_EXISTS(BAD_REQUEST, "다른 소셜 계정으로 가입된 유저입니다."),
    USER_UNFILLED(BAD_REQUEST, "유저 정보를 등록하지 않은 유저입니다."),
    IMAGE_STORAGE_DELETE_ERROR(BAD_REQUEST, "gcs에서 이미지를 삭제하는 데 오류가 발생했습니다."),
    VERIFICATION_PHOTO_ALREADY_EXISTS(BAD_REQUEST, "이미 사진 인증 처리 중인 User입니다."),
    MEMBERSHIP_ALREADY(BAD_REQUEST, "이미 멤버십에 가입한 user입니다."),
    LIKE_ALREADY_EXISTS(BAD_REQUEST, "이미 호감을 보낸 상대입니다."),
    INTRODUCTION_ALREADY_EXISTS(BAD_REQUEST, "이미 해당 질문에 답을 등록한 user입니다."),
    CHAT_ALREADY_EXISTS(BAD_REQUEST, "이미 매칭 신청을 하거나 받은 상대입니다."),
    NOT_MUTUAL_LIKE(BAD_REQUEST, "맞호감 상태의 상대가 아닙니다."),
    CHAT_NOT_PENDING(BAD_REQUEST, "상대로부터 매칭 신청을 받지 않았습니다."),

    // 401
    SECURITY_UNAUTHORIZED(UNAUTHORIZED, "인증 정보가 유효하지 않습니다"),
    SECURITY_INVALID_TOKEN(UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    SECURITY_INVALID_REFRESH_TOKEN(UNAUTHORIZED, "refresh token이 유효하지 않습니다."),

    // 403
    SECURITY_ACCESS_DENIED(FORBIDDEN, "접근 권한이 없습니다."),

    // 404
    USER_NOT_FOUND(NOT_FOUND, "user을 찾을 수 없습니다."),
    FIELD_NOT_FOUND(NOT_FOUND, "직업을 찾을 수 없습니다."),
    HOBBY_NOT_FOUND(NOT_FOUND, "취미를 찾을 수 없습니다."),
    LIFESTYLE_NOT_FOUND(NOT_FOUND, "라이프스타일을 찾을 수 없습니다."),
    WEIGHT_VALUE_NOT_FOUND(NOT_FOUND, "해당 유저의 가중치 정보를 찾을 수 없습니다."),
    EVALUATED_USER_NOT_FOUND(NOT_FOUND, "첫인상 평가 대상이 존재하지 않습니다."),
    EVALUATION_NOT_FOUND(NOT_FOUND, "첫인상 평가 대상을 찾을 수 없습니다."),
    USER_POS_NOT_FOUND(NOT_FOUND, "사용자의 위치를 찾을 수 없습니다."),
    MATCHING_NOT_FOUND(NOT_FOUND, "매칭 내역이 존재하지 않습니다."),
    MATCHING_SCORE_NOT_FOUND(NOT_FOUND, "매칭 점수 내역이 존재하지 않습니다."),
    GENDER_COUNT_NOT_FOUND(NOT_FOUND, "성별 집계를 찾을 수 없습니다."),
    MAIN_PHOTO_NOT_FOUND(NOT_FOUND, "유저의 메인 사진을 찾을 수 없습니다."),
    CHAT_NOT_FOUND(NOT_FOUND, "대화 신청 내역을 찾을 수 없습니다."),
    DEVICE_NOT_FOUND(NOT_FOUND, "auth를 찾을 수 없습니다."),
    QUESTION_NOT_FOUND(NOT_FOUND, "자기소개 질문을 찾을 수 없습니다."),
    USER_INTRODUCTION_NOT_FOUND(NOT_FOUND, "user의 자기소개를 찾을 수 없습니다."),
    USER_CAREER_NOT_FOUND(NOT_FOUND, "user의 직장 정보를 찾을 수 없습니다."),
    USER_UNIVERSITY_NOT_FOUND(NOT_FOUND, "user의 대학교를 찾을 수 없습니다."),
    USER_GRAD_NOT_FOUND(NOT_FOUND, "user의 대학원을 찾을 수 없습니다."),
    USER_INCOME_NOT_FOUND(NOT_FOUND, "user의 소득 정보를 찾을 수 없습니다."),
    USER_PLAN_NOT_FOUND(NOT_FOUND, "해당 user의 plan을 찾을 수 없습니다."),
    USER_PROFILE_NOT_FOUND(NOT_FOUND, "user의 profile을 찾을 수 없습니다."),
    VERIFICATION_PHOTO_NOT_FOUND(NOT_FOUND, "user의 인증 사진을 찾을 수 없습니다."),

    // 500
    ADDRESS_EXTRACT_ERROR(INTERNAL_SERVER_ERROR, "주소 추출에 실패했습니다."),
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "예상치 못한 서버 에러가 발생하였습니다."),
    SIGNED_URL_GENERATION_ERROR(INTERNAL_SERVER_ERROR, "스토리지에 사진을 저장하는 도중 에러가 발생하였습니다."),
    FCM_INIT_ERROR(INTERNAL_SERVER_ERROR, "FCM Admin SDK를 초기화하는 도중 오류가 발생하였습니다."),
    FAILED_TO_SEND_EMAIL(INTERNAL_SERVER_ERROR, "이메일 전송에 실패했습니다."),
    KEY_FILE_IOEXCEPTION(INTERNAL_SERVER_ERROR, "키 파일을 읽는 도중 오류가 발생했습니다."),
    PEM_PARSER_ERROR(INTERNAL_SERVER_ERROR, "pem 키를 분석하는 도중 오류가 발생했습니다."),
    ID_TOKEN_PARSER_ERROR(INTERNAL_SERVER_ERROR, "id token을 분석하는 도중 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

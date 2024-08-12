package hiff.hiff.behiff.domain.matching.exception;

import hiff.hiff.behiff.global.response.exceptionClass.CustomException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;

public class MatchingException extends CustomException {

    public MatchingException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MatchingException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}

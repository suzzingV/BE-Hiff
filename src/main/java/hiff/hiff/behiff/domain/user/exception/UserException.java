package hiff.hiff.behiff.domain.user.exception;

import hiff.hiff.behiff.global.response.exceptionClass.CustomException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;

public class UserException extends CustomException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}

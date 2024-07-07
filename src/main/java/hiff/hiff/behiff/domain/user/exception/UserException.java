package hiff.hiff.behiff.domain.user.exception;

import hiff.hiff.behiff.global.exception.exceptionClass.CustomException;
import hiff.hiff.behiff.global.exception.properties.ErrorCode;

public class UserException extends CustomException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}

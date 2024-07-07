package hiff.hiff.behiff.global.auth.exception;

import hiff.hiff.behiff.global.exception.exceptionClass.CustomException;
import hiff.hiff.behiff.global.exception.properties.ErrorCode;

public class AuthException extends CustomException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}

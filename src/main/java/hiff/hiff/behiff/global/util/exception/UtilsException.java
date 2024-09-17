package hiff.hiff.behiff.global.util.exception;

import hiff.hiff.behiff.global.response.exceptionClass.CustomException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;

public class UtilsException extends CustomException {

    public UtilsException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UtilsException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}

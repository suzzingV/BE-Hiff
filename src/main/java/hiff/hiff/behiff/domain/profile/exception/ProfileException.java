package hiff.hiff.behiff.domain.profile.exception;

import hiff.hiff.behiff.global.response.exceptionClass.CustomException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;

public class ProfileException extends CustomException {

    public ProfileException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ProfileException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}

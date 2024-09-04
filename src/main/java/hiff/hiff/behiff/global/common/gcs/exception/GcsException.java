package hiff.hiff.behiff.global.common.gcs.exception;

import hiff.hiff.behiff.global.response.exceptionClass.CustomException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;

public class GcsException extends CustomException {

    public GcsException(ErrorCode errorCode) {
        super(errorCode);
    }

    public GcsException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}

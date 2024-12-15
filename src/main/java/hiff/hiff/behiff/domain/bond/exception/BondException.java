package hiff.hiff.behiff.domain.bond.exception;

import hiff.hiff.behiff.global.response.exceptionClass.CustomException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;

public class BondException extends CustomException {

    public BondException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BondException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}

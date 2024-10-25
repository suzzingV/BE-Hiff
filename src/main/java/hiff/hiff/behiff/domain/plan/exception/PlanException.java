package hiff.hiff.behiff.domain.plan.exception;

import hiff.hiff.behiff.global.response.exceptionClass.CustomException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;

public class PlanException extends CustomException {

    public PlanException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PlanException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}

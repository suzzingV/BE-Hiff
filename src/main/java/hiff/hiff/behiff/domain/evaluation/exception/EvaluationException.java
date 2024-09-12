package hiff.hiff.behiff.domain.evaluation.exception;

import hiff.hiff.behiff.global.response.exceptionClass.CustomException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;

public class EvaluationException extends CustomException {

    public EvaluationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public EvaluationException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}

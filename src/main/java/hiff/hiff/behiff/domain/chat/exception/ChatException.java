package hiff.hiff.behiff.domain.chat.exception;

import hiff.hiff.behiff.global.response.exceptionClass.CustomException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;

public class ChatException extends CustomException {

    public ChatException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ChatException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}

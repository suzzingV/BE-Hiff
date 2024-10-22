package hiff.hiff.behiff.domain.catalog.exception;

import hiff.hiff.behiff.global.response.exceptionClass.CustomException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;

public class CatalogException extends CustomException {

    public CatalogException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CatalogException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}

package hiff.hiff.behiff.global.response;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import hiff.hiff.behiff.global.response.dto.ErrorResponse;
import hiff.hiff.behiff.global.response.dto.ValidationErrorResponse;
import hiff.hiff.behiff.global.response.exceptionClass.CustomException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(
            CustomException e, HttpServletRequest request
    ) {
        return ErrorResponse.toResponseEntity(e.getErrorCode(), e.getRuntimeValue());
    }

    @ExceptionHandler(value = {
            BindException.class,
        MethodArgumentNotValidException.class
    })
    protected ResponseEntity<List<ErrorResponse>> validationException(BindException e,
                                                                                HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        List<ErrorResponse> errors = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
            String field = fieldError.getField();
            String rejectedValue = (String)fieldError.getRejectedValue();
            String message = fieldError.getDefaultMessage();
            ErrorResponse error = ErrorResponse.builder()
                .statusCode(errorCode.getHttpStatus().value())
                .statusCodeName(errorCode.getHttpStatus().name())
                .runtimeValue(field + " : " + rejectedValue)
                .code(errorCode.name())
                .message(message)
                .build();
            errors.add(error);
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<List<ErrorResponse>> validationException(ConstraintViolationException e,
        HttpServletRequest request) {
        List<ErrorResponse> errors = new ArrayList<>();
        e.getConstraintViolations().forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            Object rejectedValue = violation.getInvalidValue();
            ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
            ErrorResponse error = ErrorResponse.builder()
                .statusCode(errorCode.getHttpStatus().value())
                .statusCodeName(errorCode.getHttpStatus().name())
                .runtimeValue(field + " : " + rejectedValue)
                .code(errorCode.name())
                .message(message)
                .build();
            errors.add(error);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        Throwable mostSpecificCause = ex.getMostSpecificCause();
        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
        if (mostSpecificCause instanceof InvalidFormatException ife) {
            String fieldName = ife.getPath().get(0).getFieldName();
            String validValues = Arrays.toString(ife.getTargetType().getEnumConstants());
            String message = String.format("값은 %s 중에 있어야 합니다.", validValues);
            ErrorResponse error = ErrorResponse.builder()
                .statusCode(errorCode.getHttpStatus().value())
                .statusCodeName(errorCode.getHttpStatus().name())
                .runtimeValue(fieldName + " : " + ife.getValue())
                .code(errorCode.name())
                .message(message)
                .build();
            return ResponseEntity.badRequest().body(error);
        } else {
            ErrorResponse error = ErrorResponse.builder()
                .statusCode(errorCode.getHttpStatus().value())
                .statusCodeName(errorCode.getHttpStatus().name())
                .runtimeValue(null)
                .code(errorCode.name())
                .message("요청값이 올바르지 않습니다.")
                .build();
            return ResponseEntity.badRequest().body(error);
        }
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(
            Exception e, HttpServletRequest request
    ) {
        return ErrorResponse.toResponseEntity(ErrorCode.SERVER_ERROR, e.getMessage());
    }
}

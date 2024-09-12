package hiff.hiff.behiff.global.auth.jwt.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hiff.hiff.behiff.global.response.dto.ErrorResponse;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) {
        setErrorResponse(response, ErrorCode.SECURITY_UNAUTHORIZED);
    }

    private void setErrorResponse(
        HttpServletResponse response,
        ErrorCode errorCode
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse errorResponse = ErrorResponse.builder()
            .statusCode(errorCode.getHttpStatus().value())
            .statusCodeName(errorCode.getHttpStatus().name())
            .code(errorCode.name())
            .message(errorCode.getMessage())
            .build();
        try {
            response.getWriter().write(objectMapper.registerModule(new JavaTimeModule())
                .writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

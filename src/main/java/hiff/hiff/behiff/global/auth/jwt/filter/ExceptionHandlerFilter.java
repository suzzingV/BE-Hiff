package hiff.hiff.behiff.global.auth.jwt.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hiff.hiff.behiff.global.auth.exception.AuthException;
import hiff.hiff.behiff.global.response.dto.ErrorResponse;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import javax.security.sasl.AuthenticationException;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (AuthException e) {
            setErrorResponse(response, e.getErrorCode());
        } catch (TokenExpiredException e) {
            setErrorResponse(response, ErrorCode.SECURITY_INVALID_TOKEN);
        } catch (AuthenticationException | JWTVerificationException e) {
            setErrorResponse(response, ErrorCode.SECURITY_UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            setErrorResponse(response, ErrorCode.SECURITY_ACCESS_DENIED);
        }
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

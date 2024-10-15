package hiff.hiff.behiff.global.auth.presentation.controller;

import hiff.hiff.behiff.domain.user.application.UserServiceFacade;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.presentation.dto.req.PhoneNumRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserUpdateResponse;
import hiff.hiff.behiff.global.auth.application.AuthService;
import hiff.hiff.behiff.global.auth.jwt.service.JwtService;
import hiff.hiff.behiff.global.auth.presentation.dto.req.FcmTokenRequest;
import hiff.hiff.behiff.global.auth.presentation.dto.req.LoginRequest;
import hiff.hiff.behiff.global.auth.presentation.dto.res.LoginResponse;
import hiff.hiff.behiff.global.auth.presentation.dto.res.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Auth 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

//    @Operation(
//            summary = "로그인",
//            description = "로그인합니다. 토큰 x"
//    )
//    @ApiResponse(
//            responseCode = "200",
//            description = "로그인에 성공하였습니다."
//    )
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
//        LoginResponse response = authService.login(request);
//        return ResponseEntity.ok(response);
//    }

    @Operation(
            summary = "토큰 재발급",
            description = "액세스 토큰과 리프레시 토큰을 재발급합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "토큰 재발급에 성공하였습니다."
    )
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissueTokens(HttpServletRequest request) {
        Optional<String> refreshToken = jwtService.extractRefreshToken(request);
        TokenResponse response = authService.reissueTokens(refreshToken);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "로그아웃",
            description = "로그아웃합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그아웃에 성공하였습니다."
    )
    @PatchMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        Optional<String> refreshToken = jwtService.extractRefreshToken(request);
        authService.invalidTokens(accessToken, refreshToken);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/fcm-token")
    public ResponseEntity<UserUpdateResponse> getFcmToken(@AuthenticationPrincipal User user, @Valid @RequestBody
        FcmTokenRequest request) {
        UserUpdateResponse response = authService.updateFcmToken(user.getId(), request);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "본인 인증 코드 전송",
            description = "본인 인증 코드를 User에게 문자로 전송합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "인증 코드 전송에 성공하였습니다."
    )
    @PostMapping("/verification-code")
    public ResponseEntity<Void> sendVerificationCode(@Valid @RequestBody PhoneNumRequest request) {
        authService.sendVerificationCode(request);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "본인 인증 코드 확인",
            description = "인증을 완료하고 로그인합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "본인 인증에 성공하였습니다."
    )
    @PatchMapping("/verification")
    public ResponseEntity<LoginResponse> sendVerificationCode(@Valid @RequestBody LoginRequest request) {
        authService.checkCode(request);
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "User 탈퇴",
            description = "User가 탈퇴합니다. 토큰 o, 리프레시 토큰도 필요(헤더 이름: Authentication-refresh)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User 탈퇴에 성공하였습니다."
    )
    @DeleteMapping
    public ResponseEntity<Void> withdraw(HttpServletRequest request,
                                         @AuthenticationPrincipal User user) {
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        Optional<String> refreshToken = jwtService.extractRefreshToken(request);
        authService.withdraw(user, accessToken, refreshToken);

        return ResponseEntity.noContent().build();
    }
}

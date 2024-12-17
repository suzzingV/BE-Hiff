package hiff.hiff.behiff.domain.user.presentation.controller;

import hiff.hiff.behiff.domain.profile.presentation.dto.res.ProfileUpdateResponse;
import hiff.hiff.behiff.domain.user.application.service.UserService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.presentation.dto.req.PhoneNumRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserInfoResponse;
import hiff.hiff.behiff.global.auth.presentation.dto.req.FcmTokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "User 관련 API")
@RestController
@RequestMapping("/api/v0/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
        summary = "내 정보 조회",
        description = "내 정보를 조회합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "내 정보 조회에 성공하였습니다."
    )
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getMyInfo(@AuthenticationPrincipal User user) {
        UserInfoResponse response = userService.getUserInfo(user.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "user 전화번호 갱신",
        description = "user의 전화번호를 갱신합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "user 전화번호 갱신에 성공하였습니다."
    )
    @PatchMapping("/phone")
    public ResponseEntity<ProfileUpdateResponse> updatePhone(@AuthenticationPrincipal User user, @Valid @RequestBody
        PhoneNumRequest request) {
        ProfileUpdateResponse response = userService.updatePhoneNum(user.getId(), request.getPhoneNum());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "fcm token 등록",
            description = "fcm token을 등록합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "fcm token 등록에 성공하였습니다."
    )
    @PatchMapping("/fcm-token")
    public ResponseEntity<ProfileUpdateResponse> getFcmToken(@AuthenticationPrincipal User user, @Valid @RequestBody
    FcmTokenRequest request) {
        ProfileUpdateResponse response = userService.updateFcmToken(user.getId(), request);

        return ResponseEntity.ok(response);
    }
}

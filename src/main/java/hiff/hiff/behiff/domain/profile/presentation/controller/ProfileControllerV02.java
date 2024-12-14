package hiff.hiff.behiff.domain.profile.presentation.controller;

import hiff.hiff.behiff.domain.profile.application.dto.UserIntroductionDto;
import hiff.hiff.behiff.domain.profile.application.service.ProfileServiceFacade;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.*;
import hiff.hiff.behiff.domain.profile.presentation.dto.res.VerificationStatusResponse;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.profile.presentation.dto.res.SignedUrlResponse;
import hiff.hiff.behiff.domain.profile.presentation.dto.res.ProfileUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Profile", description = "Profile 관련 API")
@RestController
@RequestMapping("/api/v0.2/profile")
@RequiredArgsConstructor
public class ProfileControllerV02 {

    private final ProfileServiceFacade profileServiceFacade;


    @Operation(
            summary = "user 자기소개 질문 갱신",
            description = "user 자기소개 조회합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "user 자기소개 조회에 성공하였습니다."
    )
    @GetMapping("/introduction")
    public ResponseEntity<List<UserIntroductionDto>> getUserIntroduction(@AuthenticationPrincipal User user) {
        List<UserIntroductionDto> response = profileServiceFacade.getUserIntroduction(user.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "user 자기소개 질문 갱신",
        description = "user 자기소개 질문을 갱신합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "user 자기소개 질문 갱신에 성공하였습니다."
    )
    @PatchMapping("/question")
    public ResponseEntity<ProfileUpdateResponse> updateUserQuestion(@AuthenticationPrincipal User user,
        @RequestBody @Valid UserQuestionRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updateUserQuestion(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "user 자기소개 갱신",
        description = "user 자기소개를 갱신합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "user 자기소개 갱신에 성공하였습니다."
    )
    @PatchMapping("/introduction")
    public ResponseEntity<ProfileUpdateResponse> updateIntroduction(@AuthenticationPrincipal User user,
        @RequestBody @Valid IntroductionRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updateIntroduction(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "gcs signed URL 조회",
        description = "gcs signed URL을 조회합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "gcs signed URL 조회에 성공하였습니다."
    )
    @GetMapping("/signed-url/{folder}/{file}")
    public ResponseEntity<SignedUrlResponse> getSignedUrl(@AuthenticationPrincipal User user,
                                                          @PathVariable String folder, @PathVariable String file) {
        SignedUrlResponse response = profileServiceFacade.generateSingedUrl(folder, file);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "User 사진 업데이트",
        description = "User의 사진을 업데이트합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "User 사진 업데이트에 성공하였습니다."
    )
    @PostMapping("/photo")
    public ResponseEntity<ProfileUpdateResponse> registerPhoto(@AuthenticationPrincipal User user,
        @RequestBody UserPhotoRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updatePhotos(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "User 닉네임 업데이트",
        description = "User의 닉네임을 업데이트합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "User 닉네임 업데이트에 성공하였습니다."
    )
    @PatchMapping("/nickname")
    public ResponseEntity<ProfileUpdateResponse> updateNickname(@AuthenticationPrincipal User user,
        @Valid @RequestBody
        NicknameRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updateNickname(user.getId(),
            request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "User 생년월일 업데이트",
        description = "User의 생년월일을 업데이트합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "User 생년월일 업데이트에 성공하였습니다."
    )
    @PatchMapping("/birth")
    public ResponseEntity<ProfileUpdateResponse> updateBirth(@AuthenticationPrincipal User user,
        @Valid @RequestBody
        BirthRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updateBirth(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "User 성별 업데이트",
        description = "User의 성별을 업데이트합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "User 성별 업데이트에 성공하였습니다."
    )
    @PatchMapping("/gender")
    public ResponseEntity<ProfileUpdateResponse> updateGender(@AuthenticationPrincipal User user,
        @Valid @RequestBody GenderRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updateGender(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "User mbti 업데이트",
        description = "User의 mbti를 업데이트합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "User mbti 업데이트에 성공하였습니다."
    )
    @PatchMapping("/mbti")
    public ResponseEntity<ProfileUpdateResponse> updateMbti(@AuthenticationPrincipal User user,
        @Valid @RequestBody MbtiRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updateMbti(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "User 위치 업데이트",
        description = "User의 위치를 업데이트합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "User 위치 업데이트에 성공하였습니다."
    )
    @PatchMapping("/pos")
    public ResponseEntity<ProfileUpdateResponse> updatePos(@AuthenticationPrincipal User user,
        @Valid @RequestBody PosRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updatePos(user.getId(),
            request.getLatitude(), request.getLongitude());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "User 인증사진 업데이트",
            description = "User의 인증사진을 업데이트합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User 인증사진 업데이트에 성공하였습니다."
    )
    @PatchMapping("/verification/photo")
    public ResponseEntity<ProfileUpdateResponse> updateVerficationPhoto(@AuthenticationPrincipal User user,
                                                           @Valid @RequestBody VerificationPhotoRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updateVerificationPhoto(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "User 사진 인증 상태 확인",
            description = "User 사진 인증 상태를 확인합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User 사진 인증 상태 확인에 성공하였습니다."
    )
    @GetMapping("/verification/photo/status")
    public ResponseEntity<VerificationStatusResponse> getVerificationStatus(@AuthenticationPrincipal User user) {
        VerificationStatusResponse response = profileServiceFacade.getVerificationStatus(user.getId());
        return ResponseEntity.ok(response);
    }
}

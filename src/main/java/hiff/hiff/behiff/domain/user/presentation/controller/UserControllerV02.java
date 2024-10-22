package hiff.hiff.behiff.domain.user.presentation.controller;

import hiff.hiff.behiff.domain.user.application.service.UserServiceFacade;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.presentation.dto.req.BodyTypeRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.BuddyRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.ConflictResolutionRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.ContactFrequencyRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.DrinkingRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.HeightRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.IdeologyRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.IntroductionRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.ReligionRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.SignedUrlRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.SmokingRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.FashionRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.UserQuestionRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.res.SignedUrlResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserUpdateResponse;
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
@RequestMapping("/api/v0.2/user")
@RequiredArgsConstructor
public class UserControllerV02 {

    private final UserServiceFacade userServiceFacade;

    @Operation(
        summary = "흡연 여부 갱신",
        description = "흡연 여부를 갱신합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "흡연 여부 갱신에 성공하였습니다."
    )
    @PatchMapping("/smoking")
    public ResponseEntity<UserUpdateResponse> updateSmokingStatus(@AuthenticationPrincipal User user, @RequestBody @Valid SmokingRequest request) {
        UserUpdateResponse response = userServiceFacade.updateSmokingStatus(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "음주 여부 갱신",
        description = "음주 여부를 갱신합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "음주 여부 갱신에 성공하였습니다."
    )
    @PatchMapping("/drinking")
    public ResponseEntity<UserUpdateResponse> updateDrinkingStatus(@AuthenticationPrincipal User user, @RequestBody @Valid DrinkingRequest request) {
        UserUpdateResponse response = userServiceFacade.updateDrinkingStatus(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "친한 이성 수 갱신",
        description = "친한 이성 수를 갱신합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "친한 이성 수 갱신에 성공하였습니다."
    )
    @PatchMapping("/opposite-buddy")
    public ResponseEntity<UserUpdateResponse> updateBuddyCount(@AuthenticationPrincipal User user, @RequestBody @Valid BuddyRequest request) {
        UserUpdateResponse response = userServiceFacade.updateBuddy(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "종교 갱신",
        description = "종교를 갱신합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "종교 갱신에 성공하였습니다."
    )
    @PatchMapping("/religion")
    public ResponseEntity<UserUpdateResponse> updateReligion(@AuthenticationPrincipal User user, @RequestBody @Valid ReligionRequest request) {
        UserUpdateResponse response = userServiceFacade.updateReligion(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "종교 갱신",
        description = "종교를 갱신합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "종교 갱신에 성공하였습니다."
    )
    @PatchMapping("/ideology")
    public ResponseEntity<UserUpdateResponse> updateIdeology(@AuthenticationPrincipal User user, @RequestBody @Valid IdeologyRequest request) {
        UserUpdateResponse response = userServiceFacade.updateIdeology(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "연인과의 연락 빈도 갱신",
        description = "연인과의 연락 빈도를 갱신합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "연인과의 연락 빈도 갱신에 성공하였습니다."
    )
    @PatchMapping("/contact-frequency")
    public ResponseEntity<UserUpdateResponse> updateContactFrequency(@AuthenticationPrincipal User user, @RequestBody @Valid ContactFrequencyRequest request) {
        UserUpdateResponse response = userServiceFacade.updateContactFrequency(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "연인과의 갈등 해결 갱신",
        description = "연인과의 갈등 해결을 갱신합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "연인과의 갈등 해결 갱신에 성공하였습니다."
    )
    @PatchMapping("/conflict-resolution")
    public ResponseEntity<UserUpdateResponse> updateContactFrequency(@AuthenticationPrincipal User user, @RequestBody @Valid ConflictResolutionRequest request) {
        UserUpdateResponse response = userServiceFacade.updateConflictResolution(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "유저 키 갱신",
        description = "유저 키를 갱신합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "유저 키 갱신에 성공하였습니다."
    )
    @PatchMapping("/height")
    public ResponseEntity<UserUpdateResponse> updateHeight(@AuthenticationPrincipal User user, @RequestBody @Valid HeightRequest request) {
        UserUpdateResponse response = userServiceFacade.updateHeight(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "user 체형 갱신",
        description = "user 체형을 갱신합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "user 체형 갱신에 성공하였습니다."
    )
    @PatchMapping("/body-type")
    public ResponseEntity<UserUpdateResponse> updateBodyType(@AuthenticationPrincipal User user, @RequestBody @Valid BodyTypeRequest request) {
        UserUpdateResponse response = userServiceFacade.updateBodyType(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "user 패션 갱신",
        description = "user 패션을 갱신합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "user 패션 갱신에 성공하였습니다."
    )
    @PatchMapping("/fashion")
    public ResponseEntity<UserUpdateResponse> updateFashion(@AuthenticationPrincipal User user, @RequestBody @Valid FashionRequest request) {
        UserUpdateResponse response = userServiceFacade.updateFashion(user.getId(), request);
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
    public ResponseEntity<UserUpdateResponse> updateUserQuestion(@AuthenticationPrincipal User user, @RequestBody @Valid UserQuestionRequest request) {
        UserUpdateResponse response = userServiceFacade.updateUserQuestion(user.getId(), request);
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
    public ResponseEntity<UserUpdateResponse> updateIntroduction(@AuthenticationPrincipal User user, @RequestBody @Valid IntroductionRequest request) {
        UserUpdateResponse response = userServiceFacade.updateIntroduction(user.getId(), request);
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
    @GetMapping("/signed-url")
    public ResponseEntity<SignedUrlResponse> getSignedUrl(@AuthenticationPrincipal User user, @RequestBody @Valid SignedUrlRequest request) {
        SignedUrlResponse response = userServiceFacade.generateSingedUrl(request);
        return ResponseEntity.ok(response);
    }
}

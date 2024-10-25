package hiff.hiff.behiff.domain.user.presentation.controller;

import hiff.hiff.behiff.domain.user.application.service.UserServiceFacade;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.presentation.dto.req.BirthRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.DistanceRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.EducationRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.GenderRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.HobbyRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.HopeAgeRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.LifeStyleRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.MbtiRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.NicknameRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.PosRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.SchoolRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.UserCareerRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.UserPhotoRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.WeightValueRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserInfoResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserUpdateResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserWeightValueResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "User 관련 API")
@RestController
@RequestMapping("/api/v0/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceFacade userServiceFacade;

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
        UserInfoResponse response = userServiceFacade.getUserInfo(user.getId());
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
    public ResponseEntity<UserUpdateResponse> registerPhoto(@AuthenticationPrincipal User user,
        @RequestBody UserPhotoRequest request) {
        UserUpdateResponse response = userServiceFacade.updatePhotos(user.getId(), request);
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
    public ResponseEntity<UserUpdateResponse> updateNickname(@AuthenticationPrincipal User user,
        @Valid @RequestBody
        NicknameRequest request) {
        UserUpdateResponse response = userServiceFacade.updateNickname(user.getId(),
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
    public ResponseEntity<UserUpdateResponse> updateBirth(@AuthenticationPrincipal User user,
        @Valid @RequestBody
        BirthRequest request) {
        UserUpdateResponse response = userServiceFacade.updateBirth(user.getId(), request);
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
    public ResponseEntity<UserUpdateResponse> updateGender(@AuthenticationPrincipal User user,
        @Valid @RequestBody GenderRequest request) {
        UserUpdateResponse response = userServiceFacade.updateGender(user.getId(), request);
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
    public ResponseEntity<UserUpdateResponse> updateMbti(@AuthenticationPrincipal User user,
        @Valid @RequestBody MbtiRequest request) {
        UserUpdateResponse response = userServiceFacade.updateMbti(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "User 직업 업데이트",
        description = "User의 직업을 업데이트합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "User 직업 업데이트에 성공하였습니다."
    )
    @PatchMapping("/career")
    public ResponseEntity<UserUpdateResponse> updateCareer(@AuthenticationPrincipal User user,
        @Valid @RequestBody
        UserCareerRequest request) {
        UserUpdateResponse response = userServiceFacade.updateCareer(user.getId(), request);
        return ResponseEntity.ok(response);
    }

//    @Operation(
//        summary = "User 희망나이 업데이트",
//        description = "User가 희망하는 매칭 상대의 나이를 업데이트합니다. 토큰 o"
//    )
//    @ApiResponse(
//        responseCode = "200",
//        description = "User 희망나이 업데이트에 성공하였습니다."
//    )
//    @PatchMapping("/hope-age")
//    public ResponseEntity<UserUpdateResponse> updateHopeAge(@AuthenticationPrincipal User user,
//        @Valid @RequestBody
//        HopeAgeRequest request) {
//        UserUpdateResponse response = userServiceFacade.updateHopeAge(user.getId(), request);
//        return ResponseEntity.ok(response);
//    }

    @Operation(
        summary = "User 취미 업데이트",
        description = "User의 취미를 업데이트합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "User 취미 업데이트에 성공하였습니다."
    )
    @PatchMapping("/hobby")
    public ResponseEntity<UserUpdateResponse> updateHobby(@AuthenticationPrincipal User user,
        @Valid @RequestBody HobbyRequest request) {
        UserUpdateResponse response = userServiceFacade.updateHobby(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "User 라이프스타일 업데이트",
        description = "User가 라이프스타일을 업데이트합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "User 라이프스타일 업데이트에 성공하였습니다."
    )
    @PatchMapping("/life-style")
    public ResponseEntity<UserUpdateResponse> updateLifeStyle(@AuthenticationPrincipal User user,
        @Valid @RequestBody LifeStyleRequest request) {
        UserUpdateResponse response = userServiceFacade.updateLifeStyle(user.getId(), request);
        return ResponseEntity.ok(response);
    }

//    @Operation(
//        summary = "User 희망거리 업데이트",
//        description = "User가 희망하는 매칭 상대의 거리를 업데이트합니다. 토큰 o"
//    )
//    @ApiResponse(
//        responseCode = "200",
//        description = "User 희망거리 업데이트에 성공하였습니다."
//    )
//    @PatchMapping("/distance")
//    public ResponseEntity<UserUpdateResponse> updateDistance(@AuthenticationPrincipal User user,
//        @Valid @RequestBody DistanceRequest request) {
//        UserUpdateResponse response = userServiceFacade.updateDistance(user.getId(), request);
//        return ResponseEntity.ok(response);
//    }

//    @Operation(
//        summary = "User 가중 업데이트",
//        description = "User의 매칭 가중치를 업데이트합니다. 토큰 o"
//    )
//    @ApiResponse(
//        responseCode = "200",
//        description = "User 가중치 업데이트에 성공하였습니다."
//    )
//    @PutMapping("/weight-value")
//    public ResponseEntity<UserUpdateResponse> updateWeightValue(@AuthenticationPrincipal User user,
//        @Valid @RequestBody WeightValueRequest request) {
//        UserUpdateResponse response = userServiceFacade.updateWeightValue(user.getId(), request);
//        return ResponseEntity.ok(response);
//    }

//    @Operation(
//        summary = "User 가중치 조회",
//        description = "User의 매칭 가중치를 조회합니다. 토큰 o"
//    )
//    @ApiResponse(
//        responseCode = "200",
//        description = "User 가중치 업데이트에 성공하였습니다."
//    )
//    @GetMapping("/weight-value")
//    public ResponseEntity<UserWeightValueResponse> getWeightValue(
//        @AuthenticationPrincipal User user) {
//        UserWeightValueResponse response = userServiceFacade.getWeightValue(user.getId());
//        return ResponseEntity.ok(response);
//    }

    @Operation(
        summary = "User 위치 업데이트",
        description = "User의 위치를 업데이트합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "User 위치 업데이트에 성공하였습니다."
    )
    @PatchMapping("/pos")
    public ResponseEntity<UserUpdateResponse> updatePos(@AuthenticationPrincipal User user,
        @Valid @RequestBody PosRequest request) {
        UserUpdateResponse response = userServiceFacade.updatePos(user.getId(),
            request.getLatitude(), request.getLongitude());
        return ResponseEntity.ok(response);
    }
}

package hiff.hiff.behiff.domain.user.presentation.controller;

import hiff.hiff.behiff.domain.user.application.UserServiceFacade;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.WeightValue;
import hiff.hiff.behiff.domain.user.presentation.dto.req.*;
import hiff.hiff.behiff.domain.user.presentation.dto.res.*;
import hiff.hiff.behiff.global.auth.application.AuthService;
import hiff.hiff.behiff.global.auth.jwt.service.JwtService;
import hiff.hiff.behiff.global.common.sms.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "User 관련 API")
@RestController
@RequestMapping("/api/v0/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceFacade userServiceFacade;
    private final EmailService emailService;

    @Operation(
            summary = "User 최초 정보 등록",
            description = "User의 최초 정보를 등록합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User 최초 정보 등록에 성공하였습니다."
    )
    @PostMapping("/info")
    public ResponseEntity<UserUpdateResponse> registerInfo(@AuthenticationPrincipal User user,
                                                           @Valid @RequestPart(value = "dto")
                                                           UserInfoRequest request, @RequestPart(value = "main_photo") MultipartFile mainPhoto,
                                                           @RequestPart(value = "photos") List<MultipartFile> photos) {
        UserUpdateResponse response = userServiceFacade.registerInfo(user.getId(), mainPhoto, photos, request);
        emailService.sendSignUpEmail();
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "직업 목록 조회",
        description = "직업 목록을 조회합니다. 토큰 x"
    )
    @ApiResponse(
        responseCode = "200",
        description = "직업 목록 조회에 성공하였습니다."
    )
    @GetMapping("/career/list")
    public ResponseEntity<List<TagResponse>> getCareers() {
        List<TagResponse> responses = userServiceFacade.getCareers();
        return ResponseEntity.ok(responses);
    }

    @Operation(
        summary = "취미 목록 조회",
        description = "취미 목록을 조회합니다. 토큰 x"
    )
    @ApiResponse(
        responseCode = "200",
        description = "취미 목록 조회에 성공하였습니다."
    )
    @GetMapping("/hobby/list")
    public ResponseEntity<List<TagResponse>> getHobbies() {
        List<TagResponse> responses = userServiceFacade.getHobbies();
        return ResponseEntity.ok(responses);
    }

    @Operation(
        summary = "라이프스타일 목록 조회",
        description = "라이프스타일 목록을 조회합니다. 토큰 x"
    )
    @ApiResponse(
        responseCode = "200",
        description = "라이프스타일 목록 조회에 성공하였습니다."
    )
    @GetMapping("/life-style/list")
    public ResponseEntity<List<TagResponse>> getLifeStyles() {
        List<TagResponse> responses = userServiceFacade.getLifeStyles();
        return ResponseEntity.ok(responses);
    }

    @Operation(
        summary = "내 정보 조회",
        description = "내 정보를 조회합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "내 정보 조회에 성공하였습니다."
    )
    @GetMapping("/me")
    public ResponseEntity<MyInfoResponse> getMyInfo(@AuthenticationPrincipal User user) {
        MyInfoResponse response = userServiceFacade.getMyInfo(user.getId());
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
        @RequestPart(value = "main_photo") MultipartFile mainPhoto,
        @RequestPart(value = "photos") List<MultipartFile> photos,
        @RequestPart(value = "dto") UserPhotoRequest request) {
        UserUpdateResponse response = userServiceFacade.updatePhotos(user.getId(), mainPhoto, photos, request);
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

//    @PatchMapping("/income")
//    public ResponseEntity<UserUpdateResponse> updateIncome(@AuthenticationPrincipal User user,
//        @Valid @RequestBody IncomeRequest request) {
//        UserUpdateResponse response = userService.updateIncome(user.getId(), request);
//        return ResponseEntity.ok(response);
//    }

    @Operation(
        summary = "User 학력 업데이트",
        description = "User의 학력을 업데이트합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "User 학력 업데이트에 성공하였습니다."
    )
    // TODO: 학교 인증 구현
    @PatchMapping("/education")
    public ResponseEntity<UserUpdateResponse> updateEducation(@AuthenticationPrincipal User user,
        @Valid @RequestBody
        EducationRequest request) {
        UserUpdateResponse response = userServiceFacade.updateEducation(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "User 학교 업데이트",
        description = "User의 학교를 업데이트합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "User 학교 업데이트에 성공하였습니다."
    )
    @PatchMapping("/school")
    public ResponseEntity<UserUpdateResponse> updateEducation(@AuthenticationPrincipal User user,
        @Valid @RequestBody
        SchoolRequest request) {
        UserUpdateResponse response = userServiceFacade.updateSchool(user.getId(), request);
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
        CareerRequest request) {
        UserUpdateResponse response = userServiceFacade.updateCareer(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "User 희망나이 업데이트",
        description = "User가 희망하는 매칭 상대의 나이를 업데이트합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "User 희망나이 업데이트에 성공하였습니다."
    )
    @PatchMapping("/hope-age")
    public ResponseEntity<UserUpdateResponse> updateHopeAge(@AuthenticationPrincipal User user,
        @Valid @RequestBody
        HopeAgeRequest request) {
        UserUpdateResponse response = userServiceFacade.updateHopeAge(user.getId(), request);
        return ResponseEntity.ok(response);
    }

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

    @Operation(
        summary = "User 희망거리 업데이트",
        description = "User가 희망하는 매칭 상대의 거리를 업데이트합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "User 희망거리 업데이트에 성공하였습니다."
    )
    @PatchMapping("/distance")
    public ResponseEntity<UserUpdateResponse> updateDistance(@AuthenticationPrincipal User user,
        @Valid @RequestBody DistanceRequest request) {
        UserUpdateResponse response = userServiceFacade.updateDistance(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "User 가중 업데이트",
        description = "User의 매칭 가중치를 업데이트합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "User 가중치 업데이트에 성공하였습니다."
    )
    @PutMapping("/weight-value")
    public ResponseEntity<UserUpdateResponse> updateWeightValue(@AuthenticationPrincipal User user,
                                                                @Valid @RequestBody WeightValueRequest request) {
        UserUpdateResponse response = userServiceFacade.updateWeightValue(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "User 가중치 조회",
            description = "User의 매칭 가중치를 조회합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User 가중치 업데이트에 성공하였습니다."
    )
    @GetMapping("/weight-value")
    public ResponseEntity<UserWeightValueResponse> getWeightValue(@AuthenticationPrincipal User user) {
        UserWeightValueResponse response = userServiceFacade.getWeightValue(user.getId());
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
    public ResponseEntity<UserUpdateResponse> updatePos(@AuthenticationPrincipal User user, @Valid @RequestBody PosRequest request) {
        UserUpdateResponse response = userServiceFacade.updatePos(user.getId(), request.getLatitude(), request.getLongitude());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "User 정보 입력 여부 조회",
            description = "User의 정보 입력 여부를 조회합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User 정보 입력 여부 조회에 성공하였습니다."
    )
    @GetMapping("/info-check")
    public ResponseEntity<UserIsFilledResponse> isFilled(@AuthenticationPrincipal User user) {
        UserIsFilledResponse response = userServiceFacade.isFilled(user);
        return ResponseEntity.ok(response);
    }
}

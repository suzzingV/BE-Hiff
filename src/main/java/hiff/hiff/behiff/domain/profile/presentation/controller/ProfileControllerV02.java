package hiff.hiff.behiff.domain.profile.presentation.controller;

import hiff.hiff.behiff.domain.profile.application.service.ProfileServiceFacade;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.PosRequest;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.BirthRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.BodyTypeRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.BuddyRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.ConflictResolutionRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.ContactFrequencyRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.DrinkingRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.FashionRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.GenderRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.HeightRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.HobbyRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.IdeologyRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.IntroductionRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.LifeStyleRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.MbtiRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.NicknameRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.ReligionRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.SignedUrlRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.SmokingRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.UserCareerRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.UserIncomeRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.UserPhotoRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.UserQuestionRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.UserSchoolRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.res.SignedUrlResponse;
import hiff.hiff.behiff.domain.profile.presentation.dto.res.ProfileUpdateResponse;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Profile", description = "Profile 관련 API")
@RestController
@RequestMapping("/api/v0.2/profile")
@RequiredArgsConstructor
public class ProfileControllerV02 {

    private final ProfileServiceFacade profileServiceFacade;

    @Operation(
        summary = "흡연 여부 갱신",
        description = "흡연 여부를 갱신합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "흡연 여부 갱신에 성공하였습니다."
    )
    @PatchMapping("/smoking")
    public ResponseEntity<ProfileUpdateResponse> updateSmokingStatus(
        @AuthenticationPrincipal User user, @RequestBody @Valid SmokingRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updateSmokingStatus(user.getId(), request);
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
    public ResponseEntity<ProfileUpdateResponse> updateDrinkingStatus(
        @AuthenticationPrincipal User user, @RequestBody @Valid DrinkingRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updateDrinkingStatus(user.getId(), request);
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
    public ResponseEntity<ProfileUpdateResponse> updateBuddyCount(@AuthenticationPrincipal User user,
        @RequestBody @Valid BuddyRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updateBuddy(user.getId(), request);
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
    public ResponseEntity<ProfileUpdateResponse> updateReligion(@AuthenticationPrincipal User user,
        @RequestBody @Valid ReligionRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updateReligion(user.getId(), request);
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
    public ResponseEntity<ProfileUpdateResponse> updateIdeology(@AuthenticationPrincipal User user,
        @RequestBody @Valid IdeologyRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updateIdeology(user.getId(), request);
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
    public ResponseEntity<ProfileUpdateResponse> updateContactFrequency(
        @AuthenticationPrincipal User user, @RequestBody @Valid ContactFrequencyRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updateContactFrequency(user.getId(),
            request);
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
    public ResponseEntity<ProfileUpdateResponse> updateContactFrequency(
        @AuthenticationPrincipal User user, @RequestBody @Valid ConflictResolutionRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updateConflictResolution(user.getId(),
            request);
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
    public ResponseEntity<ProfileUpdateResponse> updateHeight(@AuthenticationPrincipal User user,
        @RequestBody @Valid HeightRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updateHeight(user.getId(), request);
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
    public ResponseEntity<ProfileUpdateResponse> updateBodyType(@AuthenticationPrincipal User user,
        @RequestBody @Valid BodyTypeRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updateBodyType(user.getId(), request);
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
    public ResponseEntity<ProfileUpdateResponse> updateFashion(@AuthenticationPrincipal User user,
        @RequestBody @Valid FashionRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updateFashion(user.getId(), request);
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
    @GetMapping("/signed-url")
    public ResponseEntity<SignedUrlResponse> getSignedUrl(@AuthenticationPrincipal User user,
        @RequestBody @Valid SignedUrlRequest request) {
        SignedUrlResponse response = profileServiceFacade.generateSingedUrl(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "user 회사 갱신",
        description = "user 회사를 갱신합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "user 회사 갱신에 성공하였습니다."
    )
    @PatchMapping("/career")
    public ResponseEntity<ProfileUpdateResponse> updateCareer(@AuthenticationPrincipal User user,
        @RequestBody @Valid UserCareerRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updateCareer(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "user 대학교 등록",
        description = "user 대학교를 등록합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "user 대학교 등록에 성공하였습니다."
    )
    @PostMapping ("/university")
    public ResponseEntity<ProfileUpdateResponse> updateCareer(@AuthenticationPrincipal User user,
        @RequestBody @Valid UserSchoolRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.createUniversity(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "user 대학원 등록",
        description = "user 대학원을 등록합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "user 대학원 등록에 성공하였습니다."
    )
    @PostMapping ("/grad")
    public ResponseEntity<ProfileUpdateResponse> updateGrad(@AuthenticationPrincipal User user,
        @RequestBody @Valid UserSchoolRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.createGrad(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "user 소득 등록",
        description = "user 소득을 등록합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "user 소득 등록에 성공하였습니다."
    )
    @PostMapping ("/income")
    public ResponseEntity<ProfileUpdateResponse> updateIncome(@AuthenticationPrincipal User user,
        @RequestBody @Valid UserIncomeRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.createIncome(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "user 직업 등록",
        description = "user 직업을 등록합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "user 직업 등록에 성공하였습니다."
    )
    @PostMapping ("/career")
    public ResponseEntity<ProfileUpdateResponse> registerCareer(@AuthenticationPrincipal User user,
        @RequestBody @Valid UserCareerRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.createCareer(user.getId(), request);
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
    public ResponseEntity<ProfileUpdateResponse> updateHobby(@AuthenticationPrincipal User user,
        @Valid @RequestBody HobbyRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updateHobby(user.getId(), request);
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
    public ResponseEntity<ProfileUpdateResponse> updateLifeStyle(@AuthenticationPrincipal User user,
        @Valid @RequestBody LifeStyleRequest request) {
        ProfileUpdateResponse response = profileServiceFacade.updateLifeStyle(user.getId(), request);
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
}

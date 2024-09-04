package hiff.hiff.behiff.domain.user.presentation.controller;

import hiff.hiff.behiff.domain.user.application.UserService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.presentation.dto.req.AddressRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.BirthRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.DistanceRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.EducationRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.GenderRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.HobbyRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.HopeAgeRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.CareerRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.LifeStyleRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.MbtiRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.NicknameRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.PhoneNumRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.WeightValueRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.res.MyInfoResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.TagResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserEvaluatedScoreResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserUpdateResponse;
import hiff.hiff.behiff.global.auth.jwt.service.JwtService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "User 관련 API")
@RestController
@RequestMapping("/api/v0/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

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
        List<TagResponse> responses = userService.getCareers();
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
        List<TagResponse> responses = userService.getHobbies();
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
        List<TagResponse> responses = userService.getLifeStyles();
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
        MyInfoResponse response = userService.getMyInfo(user.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "User 사진 업데이",
            description = "User의 사진을 업데이트합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User 사진 업데이트에 성공하였습니다."
    )
    @PostMapping("/photo")
    public ResponseEntity<UserUpdateResponse> registerPhoto(@AuthenticationPrincipal User user, @RequestPart(value = "main_photo") MultipartFile mainPhoto, @RequestPart(value = "photos")List<MultipartFile> photos) {
        UserUpdateResponse response = userService.registerPhoto(user.getId(), mainPhoto, photos);
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
        @RequestBody
        NicknameRequest request) {
        UserUpdateResponse response = userService.updateNickname(user.getId(),
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
        UserUpdateResponse response = userService.updateBirth(user.getId(), request);
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
        UserUpdateResponse response = userService.updateGender(user.getId(), request);
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
        UserUpdateResponse response = userService.updateMbti(user.getId(), request);
        return ResponseEntity.ok(response);
    }

//    @PatchMapping("/income")
//    public ResponseEntity<UserUpdateResponse> updateIncome(@AuthenticationPrincipal User user,
//        @Valid @RequestBody IncomeRequest request) {
//        UserUpdateResponse response = userService.updateIncome(user.getId(), request);
//        return ResponseEntity.ok(response);
//    }

    @Operation(
            summary = "User 주소 업데이트",
            description = "User의 주소를 업데이트합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User 주소 업데이트에 성공하였습니다."
    )
    @PatchMapping("/address")
    public ResponseEntity<UserUpdateResponse> updateAddress(@AuthenticationPrincipal User user,
        @Valid @RequestBody AddressRequest request) {
        UserUpdateResponse response = userService.updateAddress(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "User 학력 업데이트",
            description = "User의 학력을 업데이트합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User 학력 업데이트에 성공하였습니다."
    )
    @PatchMapping("/education")
    public ResponseEntity<UserUpdateResponse> updateEducation(@AuthenticationPrincipal User user,
        @Valid @RequestBody
        EducationRequest request) {
        UserUpdateResponse response = userService.updateEducation(user.getId(), request);
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
        @RequestBody
        CareerRequest request) {
        UserUpdateResponse response = userService.updateCareer(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "User 전화번호 업데이트",
            description = "User의 전화번호를 업데이트합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User 전화번호 업데이트에 성공하였습니다."
    )
    @PatchMapping("/phoneNum")
    public ResponseEntity<UserUpdateResponse> updatePhoneNum(@AuthenticationPrincipal User user,
        @Valid @RequestBody
        PhoneNumRequest request) {
        UserUpdateResponse response = userService.updatePhoneNum(user.getId(), request);
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
        UserUpdateResponse response = userService.updateHopeAge(user.getId(), request);
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
        UserUpdateResponse response = userService.updateHobby(user.getId(), request);
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
        UserUpdateResponse response = userService.updateLifeStyle(user.getId(), request);
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
        UserUpdateResponse response = userService.updateDistance(user.getId(), request);
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
    public ResponseEntity<UserUpdateResponse> updateWeight(@AuthenticationPrincipal User user,
        @Valid @RequestBody WeightValueRequest request) {
        UserUpdateResponse response = userService.updateWeightValue(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "User 외모점수 조회",
            description = "User의 외모점수를 조회합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User 외모점수 조회에 성공하였습니다."
    )
    @GetMapping("/evaluated-score")
    public ResponseEntity<UserEvaluatedScoreResponse> getEvaluatedScore(
        @AuthenticationPrincipal User user) {
        UserEvaluatedScoreResponse response = userService.getEvaluatedScore(user.getId());
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
        userService.withdraw(user.getId(), accessToken, refreshToken);
        return ResponseEntity.noContent().build();
    }
}

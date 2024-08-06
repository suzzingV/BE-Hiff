package hiff.hiff.behiff.domain.user.presentation.controller;

import hiff.hiff.behiff.domain.user.application.UserService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.presentation.dto.req.*;
import hiff.hiff.behiff.domain.user.presentation.dto.res.*;
import hiff.hiff.behiff.global.auth.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/job/list")
    public ResponseEntity<List<TagResponse>> getJobs() {
        List<TagResponse> responses = userService.getJobs();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/hobby/list")
    public ResponseEntity<List<TagResponse>> getHobbies() {
        List<TagResponse> responses = userService.getHobbies();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/life-style/list")
    public ResponseEntity<List<TagResponse>> getLifeStyles() {
        List<TagResponse> responses = userService.getLifeStyles();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/me")
    public ResponseEntity<MyInfoResponse> getMyInfo(@AuthenticationPrincipal User user) {
        MyInfoResponse response = userService.getMyInfo(user.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailResponse> getUserInfo(@AuthenticationPrincipal User me, @PathVariable Long userId) {
        UserDetailResponse response = userService.getUserDetail(me.getId(), userId);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/photo")
//    public ResponseEntity<UserRegisterResponse> registerPhoto(@AuthenticationPrincipal User user, @RequestPart(value = "main photo") MultipartFile mainPhoto, @RequestPart(value = "photos")List<MultipartFile> photos) {
//        UserRegisterResponse response = userService.registerPhoto(user.getId(), photo, photos);
//        return ResponseEntity.ok(response);
//    }

    @PatchMapping("/nickname")
    public ResponseEntity<UserUpdateResponse> updateNickname(@AuthenticationPrincipal User user,
                                                             @RequestBody
                                                             NicknameRequest request) {
        UserUpdateResponse response = userService.updateNickname(user.getId(),
                request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/birth")
    public ResponseEntity<UserUpdateResponse> updateBirth(@AuthenticationPrincipal User user,
                                                          @Valid @RequestBody
                                                          BirthRequest request) {
        UserUpdateResponse response = userService.updateBirth(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/gender")
    public ResponseEntity<UserUpdateResponse> updateGender(@AuthenticationPrincipal User user,
                                                           @Valid @RequestBody GenderRequest request) {
        UserUpdateResponse response = userService.updateGender(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/mbti")
    public ResponseEntity<UserUpdateResponse> updateMbti(@AuthenticationPrincipal User user,
                                                         @Valid @RequestBody MbtiRequest request) {
        UserUpdateResponse response = userService.updateMbti(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/income")
    public ResponseEntity<UserUpdateResponse> updateIncome(@AuthenticationPrincipal User user,
                                                           @Valid @RequestBody IncomeRequest request) {
        UserUpdateResponse response = userService.updateIncome(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/address")
    public ResponseEntity<UserUpdateResponse> updateAddress(@AuthenticationPrincipal User user,
                                                            @Valid @RequestBody AddressRequest request) {
        UserUpdateResponse response = userService.updateAddress(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/education")
    public ResponseEntity<UserUpdateResponse> updateEducation(@AuthenticationPrincipal User user,
                                                              @Valid @RequestBody
                                                              EducationRequest request) {
        UserUpdateResponse response = userService.updateEducation(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/job")
    public ResponseEntity<UserUpdateResponse> updateJob(@AuthenticationPrincipal User user,
                                                        @RequestBody
                                                        JobRequest request) {
        UserUpdateResponse response = userService.updateJob(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/phoneNum")
    public ResponseEntity<UserUpdateResponse> updatePhoneNum(@AuthenticationPrincipal User user,
                                                             @Valid @RequestBody
                                                             PhoneNumRequest request) {
        UserUpdateResponse response = userService.updatePhoneNum(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/hope-age")
    public ResponseEntity<UserUpdateResponse> updateHopeAge(@AuthenticationPrincipal User user,
                                                            @Valid @RequestBody
                                                            HopeAgeRequest request) {
        UserUpdateResponse response = userService.updateHopeAge(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/hobby")
    public ResponseEntity<UserUpdateResponse> updateHobby(@AuthenticationPrincipal User user, @Valid @RequestBody HobbyRequest request) {
        UserUpdateResponse response = userService.updateHobby(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/life-style")
    public ResponseEntity<UserUpdateResponse> updateLifeStyle(@AuthenticationPrincipal User user, @Valid @RequestBody LifeStyleRequest request) {
        UserUpdateResponse response = userService.updateLifeStyle(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/distance")
    public ResponseEntity<UserUpdateResponse> updateDistance(@AuthenticationPrincipal User user, @Valid @RequestBody DistanceRequest request) {
        UserUpdateResponse response = userService.updateDistance(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/weight-value")
    public ResponseEntity<UserUpdateResponse> updateWeight(@AuthenticationPrincipal User user, @Valid @RequestBody WeightValueRequest request) {
        UserUpdateResponse response = userService.updateWeightValue(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/evaluated-score")
    public ResponseEntity<UserEvaluatedScoreResponse> getEvaluatedScore(@AuthenticationPrincipal User user) {
        UserEvaluatedScoreResponse response = userService.getEvaluatedScore(user.getId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/heart")
    public ResponseEntity<UserUpdateResponse> updateHeart(@AuthenticationPrincipal User user, @RequestBody UserHeartRequest request) {
        UserUpdateResponse response = userService.updateHeart(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> withdraw(HttpServletRequest request,
                                         @AuthenticationPrincipal User user) {
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        Optional<String> refreshToken = jwtService.extractRefreshToken(request);
        userService.withdraw(user.getId(), accessToken, refreshToken);
        return ResponseEntity.noContent().build();
    }
}

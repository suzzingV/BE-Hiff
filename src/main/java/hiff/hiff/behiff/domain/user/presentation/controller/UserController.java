package hiff.hiff.behiff.domain.user.presentation.controller;

import hiff.hiff.behiff.domain.user.application.UserService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.presentation.dto.req.*;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserRegisterResponse;
import hiff.hiff.behiff.global.auth.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

//    @PostMapping("/photo")
//    public ResponseEntity<UserRegisterResponse> registerPhoto(@AuthenticationPrincipal User user, @RequestPart(value = "photos")List<MultipartFile> photos) {
//        UserRegisterResponse response = userService.registerPhoto(user.getId(), photos);
//        return ResponseEntity.ok(response);
//    }

    @PatchMapping("/nickname")
    public ResponseEntity<UserRegisterResponse> updateNickname(@AuthenticationPrincipal User user,
        @RequestBody
        NicknameRequest request) {
        UserRegisterResponse response = userService.updateNickname(user.getId(),
            request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/birth")
    public ResponseEntity<UserRegisterResponse> updateBirth(@AuthenticationPrincipal User user,
        @Valid @RequestBody
        BirthRequest request) {
        UserRegisterResponse response = userService.updateBirth(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/gender")
    public ResponseEntity<UserRegisterResponse> updateGender(@AuthenticationPrincipal User user,
        @Valid @RequestBody GenderRequest request) {
        UserRegisterResponse response = userService.updateGender(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/mbti")
    public ResponseEntity<UserRegisterResponse> updateMbti(@AuthenticationPrincipal User user,
        @Valid @RequestBody MbtiRequest request) {
        UserRegisterResponse response = userService.updateMbti(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/income")
    public ResponseEntity<UserRegisterResponse> updateIncome(@AuthenticationPrincipal User user,
        @Valid @RequestBody IncomeRequest request) {
        UserRegisterResponse response = userService.updateIncome(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/address")
    public ResponseEntity<UserRegisterResponse> updateAddress(@AuthenticationPrincipal User user,
        @Valid @RequestBody AddressRequest request) {
        UserRegisterResponse response = userService.updateAddress(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/education")
    public ResponseEntity<UserRegisterResponse> updateEducation(@AuthenticationPrincipal User user,
        @Valid @RequestBody
        EducationRequest request) {
        UserRegisterResponse response = userService.updateEducation(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/job")
    public ResponseEntity<UserRegisterResponse> updateJob(@AuthenticationPrincipal User user,
        @RequestBody
        JobRequest request) {
        UserRegisterResponse response = userService.updateJob(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/phoneNum")
    public ResponseEntity<UserRegisterResponse> updatePhoneNum(@AuthenticationPrincipal User user,
        @Valid @RequestBody
        PhoneNumRequest request) {
        UserRegisterResponse response = userService.updatePhoneNum(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/hope-age")
    public ResponseEntity<UserRegisterResponse> updateHopeAge(@AuthenticationPrincipal User user,
        @Valid @RequestBody
        HopeAgeRequest request) {
        UserRegisterResponse response = userService.updateHopeAge(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/hobby")
    public ResponseEntity<UserRegisterResponse> updateHobby(@AuthenticationPrincipal User user, @Valid @RequestBody HobbyRequest request) {
        UserRegisterResponse response = userService.updateHobby(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/belief")
    public ResponseEntity<UserRegisterResponse> updateBelief(@AuthenticationPrincipal User user, @Valid @RequestBody BeliefRequest request) {
        UserRegisterResponse response = userService.updateBelief(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/distance")
    public ResponseEntity<UserRegisterResponse> updateDistance(@AuthenticationPrincipal User user, @Valid @RequestBody DistanceRequest request) {
        UserRegisterResponse response = userService.updateDistance(user.getId(), request);
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

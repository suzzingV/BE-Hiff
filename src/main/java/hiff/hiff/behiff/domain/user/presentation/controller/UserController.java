package hiff.hiff.behiff.domain.user.presentation.controller;

import hiff.hiff.behiff.domain.user.application.UserService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.presentation.dto.UserRegisterResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.req.BirthRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.GenderRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.MbtiRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.NicknameRequest;
import hiff.hiff.behiff.global.auth.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/nickname")
    public ResponseEntity<UserRegisterResponse> registerNickname(@AuthenticationPrincipal User user,
        @RequestBody
        NicknameRequest request) {
        UserRegisterResponse response = userService.registerNickname(user.getId(),
            request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/birth")
    public ResponseEntity<UserRegisterResponse> registerBirth(@AuthenticationPrincipal User user,
        @Valid @RequestBody
        BirthRequest request) {
        UserRegisterResponse response = userService.registerBirth(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/gender")
    public ResponseEntity<UserRegisterResponse> registerGender(@AuthenticationPrincipal User user,
        @Valid @RequestBody GenderRequest request) {
        UserRegisterResponse response = userService.registerGender(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/mbti")
    public ResponseEntity<UserRegisterResponse> registerMbti(@AuthenticationPrincipal User user,
        @Valid @RequestBody MbtiRequest request) {
        UserRegisterResponse response = userService.registerMbti(user.getId(), request);
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

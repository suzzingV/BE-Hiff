package hiff.hiff.behiff.domain.user.presentation.controller;

import hiff.hiff.behiff.domain.user.application.UserService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.presentation.dto.UserRegisterResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.req.NicknameRequest;
import hiff.hiff.behiff.global.auth.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<UserRegisterResponse> registerNickname(@AuthenticationPrincipal User user, @RequestBody
        NicknameRequest request) {
        UserRegisterResponse response = userService.registerNickname(user.getId(),
            request);
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

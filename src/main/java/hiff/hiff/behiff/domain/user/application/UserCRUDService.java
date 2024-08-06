package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.evaluation.exception.EvaluationException;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import hiff.hiff.behiff.domain.user.domain.enums.Role;
import hiff.hiff.behiff.domain.user.domain.enums.SocialType;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.global.auth.jwt.service.JwtService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCRUDService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public User registerUser(String email, String socialId, SocialType socialType,
                             Role role) {
        User user = User.builder()
                .role(role)
                .socialType(socialType)
                .socialId(socialId)
                .email(email)
                .build();
        return userRepository.save(user);
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    public User findRandomByEvaluation(Long userId, Gender gender) {
        return userRepository.findRandomByEvaluation(userId, gender)
                .orElseThrow(() -> new UserException(ErrorCode.EVALUATED_USER_NOT_FOUND));
    }

    public void withdraw(User user, Optional<String> accessToken, Optional<String> refreshToken) {
        user.delete();

        String access = accessToken
                .orElseThrow(() -> new UserException(ErrorCode.SECURITY_INVALID_ACCESS_TOKEN));
        String refresh = refreshToken
                .orElseThrow(() -> new UserException(ErrorCode.REFRESH_TOKEN_REQUIRED));

        jwtService.isTokenValid(refresh);
        jwtService.isTokenValid(access);
        jwtService.deleteRefreshToken(refresh);
        jwtService.invalidAccessToken(access);
    }
}

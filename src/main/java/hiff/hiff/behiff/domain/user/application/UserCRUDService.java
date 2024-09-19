package hiff.hiff.behiff.domain.user.application;

import static hiff.hiff.behiff.domain.user.application.UserPhotoService.PHOTOS_FOLDER_NAME;

import hiff.hiff.behiff.domain.chat.infrastructure.ChatHistoryRepository;
import hiff.hiff.behiff.domain.evaluation.infrastructure.EvaluatedUserRepository;
import hiff.hiff.behiff.domain.evaluation.infrastructure.EvaluationRepository;
import hiff.hiff.behiff.domain.matching.infrastructure.MatchingRepository;
import hiff.hiff.behiff.domain.user.domain.entity.GenderCount;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.enums.Role;
import hiff.hiff.behiff.domain.user.domain.enums.SocialType;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.GenderCountRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserHobbyRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserLifeStyleRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserPhotoRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserPosRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.domain.user.infrastructure.WeightValueRepository;
import hiff.hiff.behiff.global.auth.domain.Token;
import hiff.hiff.behiff.global.auth.exception.AuthException;
import hiff.hiff.behiff.global.auth.infrastructure.TokenRepository;
import hiff.hiff.behiff.global.auth.jwt.service.JwtService;
import hiff.hiff.behiff.global.common.gcs.GcsService;
import hiff.hiff.behiff.global.common.webClient.WebClientUtils;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import hiff.hiff.behiff.global.util.FileReader;
import hiff.hiff.behiff.global.util.Parser;
import jakarta.transaction.Transactional;
import java.security.PrivateKey;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCRUDService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final EvaluatedUserRepository evaluatedUserRepository;
    private final TokenRepository tokenRepository;
    private final ChatHistoryRepository chatHistoryRepository;
    private final EvaluationRepository evaluationRepository;
    private final GenderCountRepository genderCountRepository;
    private final MatchingRepository matchingRepository;
    private final UserHobbyRepository userHobbyRepository;
    private final UserLifeStyleRepository userLifeStyleRepository;
    private final UserPhotoRepository userPhotoRepository;
    private final GcsService gcsService;
    private final UserPosRepository userPosRepository;
    private final WeightValueRepository weightValueRepository;

    @Value("${apple.redirect-url}")
    private String appleRedirectUrl;
    @Value("${apple.credentials.key-id}")
    private String appleKeyId;
    @Value("${apple.credentials.location}")
    private String appleKeyFile;
    @Value("${apple.credentials.identifier}")
    private String appleIdentifier;
    @Value("${apple.credentials.team-id}")
    private String appleTeamId;

    public User registerUser(Role role, String socialId, SocialType socialType) {
        User user = User.builder()
            .socialId(socialId)
            .socialType(socialType)
            .role(role)
            .build();
        return userRepository.save(user);
    }


    public User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    public void checkDuplication(Long userId, String phoneNum) {
        userRepository.findByPhoneNum(phoneNum)
                .ifPresent(user -> {
                    if(user.getNickname() ==  null) {
                        userRepository.findById(userId);
                        throw new UserException(ErrorCode.USER_ALREADY_EXISTS);
                    }
                });
    }

    public void withdraw(User user, Optional<String> access, Optional<String> refresh) {
        if(user.getSocialType() ==SocialType.APPLE) {
            revokeFromApple(user);
        }
        deleteUserRecord(user);

        invalidTokens(access, refresh);
    }

    private void deleteUserRecord(User user) {
        chatHistoryRepository.deleteByProposedIdOrProposedId(user.getId(), user.getId());
        evaluatedUserRepository.deleteByUserId(user.getId());
        evaluationRepository.deleteByEvaluatedIdOrEvaluatorId(user.getId(), user.getId());
        GenderCount genderCount = genderCountRepository.findById(user.getGender())
            .orElseThrow(() -> new UserException(ErrorCode.GENDER_COUNT_NOT_FOUND));
        genderCount.subtractCount();
        matchingRepository.deleteByMatchedIdOrMatcherId(user.getId(), user.getId());
        tokenRepository.deleteByUserId(user.getId());
        userHobbyRepository.deleteByUserId(user.getId());
        userLifeStyleRepository.deleteByUserId(user.getId());
        userPosRepository.deleteByUserId(user.getId());
        weightValueRepository.deleteByUserId(user.getId());
        deletePhotos(user.getId());
        userRepository.delete(user);
    }

    private void invalidTokens(Optional<String> access, Optional<String> refresh) {
        String accessToken = access.orElseThrow(
            () -> new AuthException(ErrorCode.ACCESS_TOKEN_REQUIRED));
        String refreshToken = refresh.orElseThrow(
            () -> new AuthException(ErrorCode.REFRESH_TOKEN_REQUIRED));

        jwtService.isTokenValid(refreshToken);
        jwtService.isTokenValid(accessToken);
        jwtService.deleteRefreshToken(refreshToken);
        jwtService.invalidAccessToken(accessToken);
    }

    private void revokeFromApple(User user) {
        String clientSecret = createClientSecret();
        Token token = tokenRepository.findByUserId(user.getId())
            .orElseThrow(() -> new UserException(ErrorCode.TOKEN_NOT_FOUND));
        WebClientUtils.revokeApple(clientSecret, token.getAppleRefreshToken(), appleIdentifier);
    }

    private String createClientSecret() {
        String keyFile = FileReader.readAppleKeyFile(appleKeyFile);
        PrivateKey privateKey = Parser.getPrivateKeyFromPem(keyFile);
        return jwtService.createClientSecret(privateKey);
    }

    private void deletePhotos(Long userId) {
        userPhotoRepository.findByUserId(userId)
            .forEach(userPhoto -> {
                String photoUrl = userPhoto.getPhotoUrl();
                gcsService.deleteImage(photoUrl, PHOTOS_FOLDER_NAME);
                userPhotoRepository.delete(userPhoto);
            });
    }
}

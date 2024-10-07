package hiff.hiff.behiff.domain.user.application;

import static hiff.hiff.behiff.domain.user.application.UserPhotoService.PHOTOS_FOLDER_NAME;

import hiff.hiff.behiff.domain.chat.infrastructure.ChatHistoryRepository;
import hiff.hiff.behiff.domain.evaluation.infrastructure.EvaluatedUserRepository;
import hiff.hiff.behiff.domain.evaluation.infrastructure.EvaluationRepository;
import hiff.hiff.behiff.domain.matching.infrastructure.MatchingRepository;
import hiff.hiff.behiff.domain.user.domain.entity.GenderCount;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.enums.Role;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.GenderCountRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserHobbyRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserLifeStyleRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserPhotoRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserPosRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.domain.user.infrastructure.WeightValueRepository;
import hiff.hiff.behiff.global.auth.exception.AuthException;
import hiff.hiff.behiff.global.auth.infrastructure.TokenRepository;
import hiff.hiff.behiff.global.auth.jwt.service.JwtService;
import hiff.hiff.behiff.global.common.gcs.GcsService;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
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
    private final RedisService redisService;

    public User registerUser(Role role, String phoneNum) {
        User user = User.builder()
                .phoneNum(phoneNum)
            .role(role)
            .build();
        return userRepository.save(user);
    }


    public User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    @org.springframework.transaction.annotation.Transactional(noRollbackFor = UserException.class)
    public void checkDuplication(Long userId, String phoneNum) {
        userRepository.findByPhoneNum(phoneNum)
                .ifPresent(user -> {
                    deleteById(userId);
                    throw new UserException(ErrorCode.USER_ALREADY_EXISTS);
                });
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteUserRecord(User user) {
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

    private void deletePhotos(Long userId) {
        userPhotoRepository.findByUserId(userId)
            .forEach(userPhoto -> {
                String photoUrl = userPhoto.getPhotoUrl();
                gcsService.deleteImage(photoUrl, PHOTOS_FOLDER_NAME);
                userPhotoRepository.delete(userPhoto);
            });
    }

    protected void deleteById(Long userId) {
        // TODO: 삭제 고치기
        log.info("userId: " + userId);
        userRepository.deleteById(userId);
        userRepository.flush();
    }
}

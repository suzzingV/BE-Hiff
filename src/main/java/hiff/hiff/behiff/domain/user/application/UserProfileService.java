package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.user.domain.entity.GenderCount;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.UserPos;
import hiff.hiff.behiff.domain.user.domain.enums.Education;
import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import hiff.hiff.behiff.domain.user.domain.enums.Mbti;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.GenderCountRepository;
import hiff.hiff.behiff.domain.user.infrastructure.MbtiScoreRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserPosRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserPosRepository userPosRepository;
    private final GenderCountRepository genderCountRepository;
    private final UserCRUDService userCRUDService;
    private final MbtiScoreRepository mbtiScoreRepository;
    private final RedisService redisService;

    //    public static final String INCOME_PREFIX = "income_";
    public static final String MBTI_PREFIX = "mbti_";

    public void updateNickname(User user, String nickname) {
        checkNicknameDuplication(nickname);
        user.changeNickname(nickname);
    }

    public void updateBirth(User user, Integer birthYear, Integer birthMonth, Integer birthDay) {
        user.changeBirth(birthYear, birthMonth, birthDay);
    }

    public void updateGender(User user, Gender gender) {
        user.changeGender(gender);
        updateGenderCount(gender);
    }

    private void updateGenderCount(Gender gender) {
        GenderCount genderCount = genderCountRepository.findById(gender)
            .orElseThrow(() -> new UserException(ErrorCode.GENDER_COUNT_NOT_FOUND));
        genderCount.addCount();
    }

    public void updateMbti(User user, Mbti mbti) {
        user.changeMbti(mbti);
    }

//    public void updateIncome(User user, IncomeRequest request) {
//        user.changeIncome(request.getIncome());
//    }

    public void updateEducation(User user, Education education) {
        user.changeEducation(education);
    }

    public void updateSchool(User user, String school) {
        user.changeSchool(school);
    }

    public void updatePhoneNum(Long userId, String phoneNum) {
        User user = userCRUDService.findById(userId);
        user.changePhoneNum(phoneNum);
    }

    public void updateHopeAge(User user, Integer maxAge, Integer minAge) {
        user.changeHopeAge(maxAge, minAge);
    }

    public void updateDistance(User user, Integer maxDistance, Integer minDistance) {
        checkDistanceRange(maxDistance, minDistance);
        user.changeMaxDistance(maxDistance);
        user.changeMinDistance(minDistance);
    }

    public Optional<UserPos> findUserPosByUserId(Long userId) {
        return userPosRepository.findByUserId(userId);
    }

    public void cacheMbtiSimilarity() {
        mbtiScoreRepository.findAll()
                .forEach(mbtiScore -> {
                    String mbti1 = mbtiScore.getId().getMbti1();
                    String mbti2 = mbtiScore.getId().getMbti2();
                    int similarity = mbtiScore.getScore();
                    redisService.setValue(MBTI_PREFIX + mbti1 + "_" + mbti2, String.valueOf(similarity));
                });
    }

    private void checkNicknameDuplication(String nickname) {
        userRepository.findByNickname(nickname)
            .ifPresent(user -> {
                throw new UserException(ErrorCode.NICKNAME_ALREADY_EXISTS);
            });
    }

    private static void checkDistanceRange(Integer maxDistance, Integer minDistance) {
        if (minDistance > maxDistance) {
            throw new UserException(ErrorCode.DISTANCE_RANGE_REVERSE);
        }
    }

    public Double getEvaluatedScore(User user) {
        return user.getEvaluatedScore();
    }
}

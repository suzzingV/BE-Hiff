package hiff.hiff.behiff.domain.profile.application.service;

import hiff.hiff.behiff.domain.catalog.domain.entity.GenderCount;
import hiff.hiff.behiff.domain.catalog.infrastructure.GenderCountRepository;
import hiff.hiff.behiff.domain.catalog.infrastructure.MbtiScoreRepository;
import hiff.hiff.behiff.domain.profile.domain.entity.UserProfile;
import hiff.hiff.behiff.domain.profile.exception.ProfileException;
import hiff.hiff.behiff.domain.profile.infrastructure.UserProfileRepository;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.profile.domain.enums.BodyType;
import hiff.hiff.behiff.domain.profile.domain.enums.Buddy;
import hiff.hiff.behiff.domain.profile.domain.enums.ConflictResolution;
import hiff.hiff.behiff.domain.profile.domain.enums.ContactFrequency;
import hiff.hiff.behiff.domain.profile.domain.enums.Drinking;
import hiff.hiff.behiff.domain.profile.domain.enums.Gender;
import hiff.hiff.behiff.domain.profile.domain.enums.Ideology;
import hiff.hiff.behiff.domain.profile.domain.enums.Mbti;
import hiff.hiff.behiff.domain.profile.domain.enums.Religion;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserProfileService {

    private final GenderCountRepository genderCountRepository;
    private final MbtiScoreRepository mbtiScoreRepository;
    private final RedisService redisService;
    private final UserProfileRepository userProfileRepository;

    //    public static final String INCOME_PREFIX = "income_";
    public static final String MBTI_PREFIX = "mbti_";

    public void updateNickname(UserProfile userProfile, String nickname) {
        checkNicknameDuplication(nickname);
        userProfile.changeNickname(nickname);
    }

    public void updateBirth(UserProfile userProfile, Integer birthYear, Integer birthMonth, Integer birthDay) {
        userProfile.changeBirth(birthYear, birthMonth, birthDay);
    }

    public void updateGender(UserProfile userProfile, Gender gender) {
        userProfile.changeGender(gender);
        updateGenderCount(gender);
    }

    private void updateGenderCount(Gender gender) {
        GenderCount genderCount = genderCountRepository.findById(gender)
            .orElseThrow(() -> new UserException(ErrorCode.GENDER_COUNT_NOT_FOUND));
        genderCount.addCount();
    }

    public void updateMbti(UserProfile userProfile, Mbti mbti) {
        userProfile.changeMbti(mbti);
    }

    public void updateSmokingStatus(UserProfile userProfile, Boolean isSmoking) {
        userProfile.changeIsSmoking(isSmoking);
    }

    public void updateDrinkingStatus(UserProfile userProfile, Drinking drinking) {
        userProfile.changeDrinking(drinking);
    }

    public void updateBuddy(UserProfile userProfile, Buddy buddy) {
        userProfile.changeBuddy(buddy);
    }

    public void cacheMbtiSimilarity() {
        mbtiScoreRepository.findAll()
            .forEach(mbtiScore -> {
                String mbti1 = mbtiScore.getId().getMbti1();
                String mbti2 = mbtiScore.getId().getMbti2();
                int similarity = mbtiScore.getScore();
                redisService.setValue(MBTI_PREFIX + mbti1 + "_" + mbti2,
                    String.valueOf(similarity));
            });
    }

    private void checkNicknameDuplication(String nickname) {
        userProfileRepository.findByNickname(nickname)
            .ifPresent(user -> {
                throw new ProfileException(ErrorCode.NICKNAME_ALREADY_EXISTS);
            });
    }

    private static void checkDistanceRange(Integer maxDistance, Integer minDistance) {
        if (minDistance > maxDistance) {
            throw new UserException(ErrorCode.DISTANCE_RANGE_REVERSE);
        }
    }

    public Double getEvaluatedScore(UserProfile userProfile) {
        return userProfile.getEvaluatedScore();
    }

    public void updateReligion(UserProfile userProfile, Religion religion) {
        userProfile.changeReligion(religion);
    }

    public void updateIdeology(UserProfile userProfile, Ideology ideology) {
        userProfile.changeIdeology(ideology);
    }

    public void updateContactFrequency(UserProfile userProfile, ContactFrequency contactFrequency) {
        userProfile.changeContactFrequency(contactFrequency);
    }

    public void updateConflictResolution(UserProfile userProfile, ConflictResolution conflictResolution) {
        userProfile.changeConflictResolution(conflictResolution);
    }

    public void updateHeight(UserProfile userProfile, Integer height) {
        userProfile.changeHeight(height);
    }

    public void updateBodyType(UserProfile userProfile, BodyType bodyType) {
        userProfile.changeBodyType(bodyType);
    }

    public UserProfile findByUserId(Long userId) {
        return userProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ProfileException(ErrorCode.USER_PROFILE_NOT_FOUND));
    }

    public void deleteByUserId(Long userId) {
        userProfileRepository.deleteByUserId(userId);
    }

    public void createUserProfile(Long userId) {
        UserProfile userProfile = UserProfile.builder()
            .userId(userId)
            .build();
        userProfileRepository.save(userProfile);
    }
}

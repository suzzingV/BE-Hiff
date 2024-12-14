package hiff.hiff.behiff.domain.profile.application.service;

import hiff.hiff.behiff.domain.catalog.domain.entity.GenderCount;
import hiff.hiff.behiff.domain.catalog.infrastructure.GenderCountRepository;
import hiff.hiff.behiff.domain.profile.application.dto.UserIntroductionDto;
import hiff.hiff.behiff.domain.profile.domain.entity.UserProfile;
import hiff.hiff.behiff.domain.profile.domain.enums.Gender;
import hiff.hiff.behiff.domain.profile.domain.enums.VerificationStatus;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.*;
import hiff.hiff.behiff.domain.profile.presentation.dto.res.SignedUrlResponse;
import hiff.hiff.behiff.domain.profile.presentation.dto.res.ProfileUpdateResponse;
import hiff.hiff.behiff.domain.profile.presentation.dto.res.VerificationStatusResponse;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProfileServiceFacade {

    private final UserPhotoService userPhotoService;
    private final UserProfileService userProfileService;
    private final UserIntroductionService userIntroductionService;
    private final GenderCountRepository genderCountRepository;
    private final UserPosService userPosService;
    private final VerificationPhotoService verificationPhotoService;

    public ProfileUpdateResponse updatePhotos(Long userId, UserPhotoRequest request) {
        UserProfile userProfile = userProfileService.findByUserId(userId);
        userPhotoService.registerMainPhoto(userProfile, request.getNewMainPhoto());
        userPhotoService.registerPhotos(userId, request.getNewPhotos());
        userPhotoService.deletePhotos(request.getTrashPhotos());
        return ProfileUpdateResponse.from(userId);
    }

    public ProfileUpdateResponse updateNickname(Long userId, NicknameRequest request) {
        UserProfile userProfile = userProfileService.findByUserId(userId);
        userProfileService.updateNickname(userProfile, request.getNickname());
        return ProfileUpdateResponse.from(userId);
    }

    public ProfileUpdateResponse updateBirth(Long userId, BirthRequest request) {
        UserProfile userProfile = userProfileService.findByUserId(userId);
        userProfileService.updateBirth(userProfile, request.getBirthYear(), request.getBirthMonth(),
            request.getBirthDay());
        return ProfileUpdateResponse.from(userId);
    }

    public ProfileUpdateResponse updateGender(Long userId, GenderRequest request) {
        UserProfile userProfile = userProfileService.findByUserId(userId);
        userProfileService.updateGender(userProfile, request.getGender());
        return ProfileUpdateResponse.from(userId);
    }

    public ProfileUpdateResponse updateMbti(Long userId, MbtiRequest request) {
        UserProfile userProfile = userProfileService.findByUserId(userId);
        userProfileService.updateMbti(userProfile, request.getMbti());
        return ProfileUpdateResponse.from(userId);
    }

    public ProfileUpdateResponse updateIntroduction(Long userId, IntroductionRequest request) {
        userIntroductionService.updateIntroduction(userId, request.getQuestionId(),
            request.getContent());
        return ProfileUpdateResponse.from(userId);
    }

    public ProfileUpdateResponse updateUserQuestion(Long userId, UserQuestionRequest request) {
        userIntroductionService.registerUserIntroduction(userId, request);
        return ProfileUpdateResponse.from(userId);
    }

    public SignedUrlResponse generateSingedUrl(String folder, String file) {
        return userPhotoService.generateSingedUrl(folder, file);
    }

    public ProfileUpdateResponse updatePos(Long userId, Double x, Double y) {
        return userPosService.updatePos(userId, x, y);
    }

    public void deleteByUserId(Long userId) {
        UserProfile userProfile = userProfileService.findByUserId(userId);
        subtractGenderCount(userProfile.getGender());
//        matchingRepository.deleteByMatchedIdOrMatcherId(userId, userId);
        userIntroductionService.deleteByUserId(userId);
        userPosService.deleteByUserId(userId);
        userProfileService.deleteByUserId(userId);
        userPhotoService.deletePhotos(userId);
    }

    public ProfileUpdateResponse updateVerificationPhoto(Long userId, VerificationPhotoRequest request) {
        verificationPhotoService.registerVerificationPhoto(userId, request.getVerificationPhoto());
        return ProfileUpdateResponse.from(userId);
    }

    public VerificationStatusResponse getVerificationStatus(Long userId) {
        VerificationStatus status = verificationPhotoService.getStatus(userId);
        return VerificationStatusResponse.builder()
                .status(status)
                .build();
    }

    private void subtractGenderCount(Gender gender) {
        if (gender != null) {
            GenderCount genderCount = genderCountRepository.findById(gender)
                .orElseThrow(() -> new UserException(ErrorCode.GENDER_COUNT_NOT_FOUND));
            genderCount.subtractCount();
        }
    }

    public List<UserIntroductionDto> getUserIntroduction(Long userId) {
        return userIntroductionService.findIntroductionByUserId(userId);
    }
}

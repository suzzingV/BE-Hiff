package hiff.hiff.behiff.domain.profile.application.service;

import hiff.hiff.behiff.domain.catalog.domain.entity.GenderCount;
import hiff.hiff.behiff.domain.catalog.infrastructure.GenderCountRepository;
import hiff.hiff.behiff.domain.profile.domain.entity.UserProfile;
import hiff.hiff.behiff.domain.profile.domain.enums.Gender;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.BirthRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.BodyTypeRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.BuddyRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.ConflictResolutionRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.ContactFrequencyRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.DrinkingRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.FashionRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.GenderRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.HeightRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.HobbyRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.IdeologyRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.IntroductionRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.LifeStyleRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.MbtiRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.NicknameRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.ReligionRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.SignedUrlRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.SmokingRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.UserCareerRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.UserIncomeRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.UserPhotoRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.UserQuestionRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.UserSchoolRequest;
import hiff.hiff.behiff.domain.profile.presentation.dto.res.SignedUrlResponse;
import hiff.hiff.behiff.domain.profile.presentation.dto.res.ProfileUpdateResponse;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProfileServiceFacade {

    private final UserHobbyService userHobbyService;
    private final UserLifeStyleService userLifeStyleService;
    private final UserCareerService userCareerService;
    private final UserPhotoService userPhotoService;
    private final UserProfileService userProfileService;
    private final UserFashionService userFashionService;
    private final UserIntroductionService userIntroductionService;
    private final UserSchoolService userSchoolService;
    private final UserIncomeService userIncomeService;
    private final GenderCountRepository genderCountRepository;
    private final UserPosService userPosService;

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

    public ProfileUpdateResponse updateCareer(Long userId, UserCareerRequest request) {
        userCareerService.updateCareer(userId, request.getField(), request.getCompany(), request.getVerification());
        return ProfileUpdateResponse.from(userId);
    }

    public ProfileUpdateResponse updateHobby(Long userId, HobbyRequest request) {
        return userHobbyService.updateHobby(userId, request.getOriginHobbies());
    }

    public ProfileUpdateResponse updateLifeStyle(Long userId, LifeStyleRequest request) {
        return userLifeStyleService.updateLifeStyle(userId, request.getOriginLifeStyles());
    }

    public ProfileUpdateResponse updateSmokingStatus(Long userId, SmokingRequest request) {
        UserProfile userProfile = userProfileService.findByUserId(userId);
        userProfileService.updateSmokingStatus(userProfile, request.getIsSmoking());
        return ProfileUpdateResponse.from(userId);
    }

    public ProfileUpdateResponse updateDrinkingStatus(Long userId, DrinkingRequest request) {
        UserProfile userProfile = userProfileService.findByUserId(userId);
        userProfileService.updateDrinkingStatus(userProfile, request.getDrinking());
        return ProfileUpdateResponse.from(userId);
    }

    public ProfileUpdateResponse updateBuddy(Long userId, BuddyRequest request) {
        UserProfile userProfile = userProfileService.findByUserId(userId);
        userProfileService.updateBuddy(userProfile, request.getBuddy());
        return ProfileUpdateResponse.from(userId);
    }

    public ProfileUpdateResponse updateReligion(Long userId, ReligionRequest request) {
        UserProfile userProfile = userProfileService.findByUserId(userId);
        userProfileService.updateReligion(userProfile, request.getReligion());
        return ProfileUpdateResponse.from(userId);
    }

    public ProfileUpdateResponse updateIdeology(Long userId, IdeologyRequest request) {
        UserProfile userProfile = userProfileService.findByUserId(userId);
        userProfileService.updateIdeology(userProfile, request.getIdeology());
        return ProfileUpdateResponse.from(userId);
    }

    public ProfileUpdateResponse updateContactFrequency(Long userId, ContactFrequencyRequest request) {
        UserProfile userProfile = userProfileService.findByUserId(userId);
        userProfileService.updateContactFrequency(userProfile, request.getContactFrequency());
        return ProfileUpdateResponse.from(userId);
    }

    public ProfileUpdateResponse updateConflictResolution(Long userId,
        ConflictResolutionRequest request) {
        UserProfile userProfile = userProfileService.findByUserId(userId);
        userProfileService.updateConflictResolution(userProfile, request.getConflictResolution());
        return ProfileUpdateResponse.from(userId);
    }

    public ProfileUpdateResponse updateHeight(Long userId, HeightRequest request) {
        UserProfile userProfile = userProfileService.findByUserId(userId);
        userProfileService.updateHeight(userProfile, request.getHeight());
        return ProfileUpdateResponse.from(userId);
    }

    public ProfileUpdateResponse updateBodyType(Long userId, BodyTypeRequest request) {
        UserProfile userProfile = userProfileService.findByUserId(userId);
        userProfileService.updateBodyType(userProfile, request.getBodyType());
        return ProfileUpdateResponse.from(userId);
    }

    public ProfileUpdateResponse updateFashion(Long userId, FashionRequest request) {
        userFashionService.updateFashion(userId, request.getFashions());
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

    public ProfileUpdateResponse createUniversity(Long userId, UserSchoolRequest request) {
        userSchoolService.createUniversity(userId, request.getName(), request.getVerification());
        return ProfileUpdateResponse.from(userId);
    }

    public ProfileUpdateResponse createGrad(Long userId, UserSchoolRequest request) {
        userSchoolService.createGrad(userId, request.getName(), request.getVerification());
        return ProfileUpdateResponse.from(userId);
    }

    public ProfileUpdateResponse createIncome(Long userId, UserIncomeRequest request) {
        userIncomeService.createIncome(userId, request.getIncome(), request.getVerification());
        return ProfileUpdateResponse.from(userId);
    }

    public ProfileUpdateResponse createCareer(Long userId, UserCareerRequest request) {
        userCareerService.createCareer(userId, request.getCompany(), request.getField(), request.getVerification());
        return ProfileUpdateResponse.from(userId);
    }

    public ProfileUpdateResponse updatePos(Long userId, Double x, Double y) {
        return userPosService.updatePos(userId, x, y);
    }

    public void deleteByUserId(Long userId) {
        UserProfile userProfile = userProfileService.findByUserId(userId);
        subtractGenderCount(userProfile.getGender());
//        matchingRepository.deleteByMatchedIdOrMatcherId(userId, userId);
        userCareerService.deleteByUserId(userId);
        userFashionService.deleteByUserId(userId);
        userSchoolService.deleteUniversityByUserId(userId);
        userHobbyService.deleteByUserId(userId);
        userIncomeService.deleteByUserId(userId);
        userIntroductionService.deleteByUserId(userId);
        userLifeStyleService.deleteByUserId(userId);
        userPosService.deleteByUserId(userId);
        userProfileService.deleteByUserId(userId);
        userSchoolService.deleteGradByUserId(userId);
        userPhotoService.deletePhotos(userId);
    }

    private void subtractGenderCount(Gender gender) {
        if (gender != null) {
            GenderCount genderCount = genderCountRepository.findById(gender)
                .orElseThrow(() -> new UserException(ErrorCode.GENDER_COUNT_NOT_FOUND));
            genderCount.subtractCount();
        }
    }
}

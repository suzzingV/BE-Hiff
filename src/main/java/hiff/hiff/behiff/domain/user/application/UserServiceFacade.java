package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.evaluation.application.EvaluationService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.WeightValue;
import hiff.hiff.behiff.domain.user.domain.enums.Role;
import hiff.hiff.behiff.domain.user.domain.enums.SocialType;
import hiff.hiff.behiff.domain.user.presentation.dto.req.*;
import hiff.hiff.behiff.domain.user.presentation.dto.res.MyInfoResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.TagResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserEvaluatedScoreResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserUpdateResponse;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceFacade {

    private final UserHobbyService userHobbyService;
    private final UserLifeStyleService userLifeStyleService;
    private final UserCareerService userCareerService;
    private final UserWeightValueService userWeightValueService;
    private final UserPhotoService userPhotoService;
    private final UserProfileService userProfileService;
    private final UserCRUDService userCRUDService;
    private final UserPosService userPosService;
    private final UserIdentifyVerificationService userIdentifyVerificationService;
    private final EvaluationService evaluationService;

    public User registerUser(
        Role role, String socialId, SocialType socialType, Double lat, Double lon) {
        User user = userCRUDService.registerUser(role, socialId, socialType);
        userWeightValueService.createWeightValue(user.getId());
        userPosService.createPos(user.getId(), lat, lon);
        evaluationService.addEvaluatedUser(user.getId(), user.getGender());
        evaluationService.addEvaluatedUser(user.getId(), user.getGender());
        return user;
    }

    public UserUpdateResponse registerInfo(Long userId, MultipartFile mainPhoto, List<MultipartFile> photos, UserInfoRequest request) {
        User user = userCRUDService.findById(userId);
        WeightValue wv = userWeightValueService.findByUserId(userId);
        userProfileService.updateNickname(user, request.getNickname());
        userPhotoService.registerPhoto(userId, mainPhoto, photos);
        userProfileService.updateBirth(user, request.getBirthYear(), request.getBirthMonth(), request.getBirthDay());
        userProfileService.updateGender(user, request.getGender());
        userProfileService.updateMbti(user, request.getMbti());
        userCareerService.updateOriginCareer(user, request.getCareerId());
        userHobbyService.updateHobby(userId, request.getOriginHobbies());
        userLifeStyleService.updateLifeStyle(userId, request.getOriginLifeStyles());
        userPhotoService.registerPhoto(userId, mainPhoto, photos);
        wv.changeWeightValue(request.getAppearanceWV(), request.getHobbyWV(), request.getLifeStyleWV(), request.getMbtiWV());
        userProfileService.updateDistance(user, request.getMaxDistance(), request.getMinDistance());
        userProfileService.updateHopeAge(user, request.getMaxAge(), request.getMinAge());

        return UserUpdateResponse.from(userId);
    }

    public User findById(Long userId) {
        return userCRUDService.findById(userId);
    }

    public void withdraw(Long userId, Optional<String> accessToken, Optional<String> refreshToken) {
        User user = userCRUDService.findById(userId);
        userCRUDService.withdraw(user, accessToken, refreshToken);
    }

    public UserUpdateResponse registerPhoto(Long userId, MultipartFile mainPhoto,
        List<MultipartFile> photos) {
        userCRUDService.findById(userId);
        userPhotoService.registerPhoto(userId, mainPhoto, photos);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateNickname(Long userId, NicknameRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateNickname(user, request.getNickname());
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateBirth(Long userId, BirthRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateBirth(user, request.getBirthYear(), request.getBirthMonth(), request.getBirthDay());
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateGender(Long userId, GenderRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateGender(user, request.getGender());
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateMbti(Long userId, MbtiRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateMbti(user, request.getMbti());
        return UserUpdateResponse.from(userId);
    }

//    public UserUpdateResponse updateIncome(Long userId, IncomeRequest request) {
//        User user = userCRUDService.findById(userId);
//        userProfileService.updateIncome(user, request);
//        return UserUpdateResponse.from(userId);
//    }

    public UserUpdateResponse updateEducation(Long userId, EducationRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateEducation(user, request.getEducation());
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateSchool(Long userId, SchoolRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateSchool(user, request.getSchool());
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateHopeAge(Long userId, HopeAgeRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateHopeAge(user, request.getMaxAge(), request.getMinAge());
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateCareer(Long userId, CareerRequest request) {
        User user = userCRUDService.findById(userId);
//        if (request.getCareerId() != null && request.getNewCareerName() == null) {
            userCareerService.updateOriginCareer(user, request.getCareerId());
//        } else if (request.getNewCareerName() != null && request.getCareerId() == null) {
//            userCareerService.updateNewCareer(user, request.getNewCareerName());
//        } else {
//            throw new UserException(ErrorCode.CAREER_UPDATE_REQUEST_ERROR);
//        }
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateHobby(Long userId, HobbyRequest request) {
        userCRUDService.findById(userId);
        return userHobbyService.updateHobby(userId, request.getOriginHobbies());
    }

    public UserUpdateResponse updateLifeStyle(Long userId, LifeStyleRequest request) {
        userCRUDService.findById(userId);
        return userLifeStyleService.updateLifeStyle(userId, request.getOriginLifeStyles());
    }

    public UserUpdateResponse updateDistance(Long userId, DistanceRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateDistance(user, request.getMaxDistance(), request.getMinDistance());
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateWeightValue(Long userId, WeightValueRequest request) {
        userCRUDService.findById(userId);
        userWeightValueService.updateWeightValue(userId, request.getAppearance(), request.getHobby(), request.getLifeStyle(), request.getMbti());
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updatePos(Long userId, Double x, Double y) {
        return userPosService.updatePos(userId, x, y);
    }

    public MyInfoResponse getMyInfo(Long userId) {
        User user = userCRUDService.findById(userId);
        String mainPhoto = user.getMainPhoto();
        List<String> photos = userPhotoService.getPhotosOfUser(userId);
        List<String> hobbies = userHobbyService.findNameByUser(userId);
        List<String> lifeStyles = userLifeStyleService.findNamesByUser(userId);
        WeightValue weightValue = userWeightValueService.findByUserId(userId);

        return MyInfoResponse.of(user, hobbies, mainPhoto, photos, lifeStyles, weightValue);
    }

    @Transactional(readOnly = true)
    public List<TagResponse> getHobbies() {
        return userHobbyService.getAllHobbies()
            .stream().map(hobby ->
                TagResponse.builder()
                    .id(hobby.getId())
                    .name(hobby.getName())
                    .build())
            .toList();
    }

    @Transactional(readOnly = true)
    public List<TagResponse> getLifeStyles() {
        return userLifeStyleService.getAllLifeStyles()
            .stream().map(lifeStyle ->
                TagResponse.builder()
                    .id(lifeStyle.getId())
                    .name(lifeStyle.getName())
                    .build())
            .toList();
    }

    @Transactional(readOnly = true)
    public List<TagResponse> getCareers() {
        return userCareerService.getAllCareers()
            .stream().map(career ->
                TagResponse.builder()
                    .id(career.getId())
                    .name(career.getName())
                    .build())
            .toList();
    }

    public UserEvaluatedScoreResponse getEvaluatedScore(Long userId) {
        User user = userCRUDService.findById(userId);
        Double score = userProfileService.getEvaluatedScore(user);
        return UserEvaluatedScoreResponse.builder()
            .evaluatedScore(score)
            .userId(userId)
            .build();
    }

    public void sendVerificationCode(Long userId, PhoneNumRequest request) {
        User user = userCRUDService.findById(userId);
        userCRUDService.checkDuplication(request.getPhoneNum());
        userIdentifyVerificationService.sendIdentificationSms(user, request.getPhoneNum());
    }

    public void identifyVerification(Long userId, VerificationCodeRequest request) {
        userIdentifyVerificationService.checkCode(userId, request.getCode());
        userProfileService.updatePhoneNum(userId, request.getPhoneNum());
    }
}

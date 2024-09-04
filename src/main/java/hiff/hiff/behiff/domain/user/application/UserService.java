package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.evaluation.application.EvaluationService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.WeightValue;
import hiff.hiff.behiff.domain.user.domain.enums.Role;
import hiff.hiff.behiff.domain.user.domain.enums.SocialType;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.presentation.dto.req.AddressRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.BirthRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.DistanceRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.EducationRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.GenderRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.HobbyRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.HopeAgeRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.IncomeRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.CareerRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.LifeStyleRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.MbtiRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.NicknameRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.PhoneNumRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.WeightValueRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.res.MyInfoResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.TagResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserEvaluatedScoreResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserUpdateResponse;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserHobbyService userHobbyService;
    private final UserLifeStyleService userLifeStyleService;
    private final UserCareerService userCareerService;
    private final UserWeightValueService userWeightValueService;
    private final UserPhotoService userPhotoService;
    private final UserProfileService userProfileService;
    private final UserCRUDService userCRUDService;
    private final UserPosService userPosService;
    private final EvaluationService evaluationService;

    public User registerUser(String email, String socialId, SocialType socialType,
        Role role, Double x, Double y) {
        User user = userCRUDService.registerUser(email, socialId, socialType, role);
        userWeightValueService.createWeightValue(user.getId());
        userPosService.createPos(user.getId(), x, y);
        evaluationService.addEvaluatedUser(user.getId(), user.getGender());
        evaluationService.addEvaluatedUser(user.getId(), user.getGender());
        return user;
    }

    public User findByEmail(String email) {
        return userCRUDService.findByEmail(email);
    }

    public void createPos(Long userId, Double x, Double y) {
        userPosService.createPos(userId, x, y);
    }

    public void withdraw(Long userId, Optional<String> accessToken, Optional<String> refreshToken) {
        User user = userCRUDService.findById(userId);
        userCRUDService.withdraw(user, accessToken, refreshToken);
    }

    public UserUpdateResponse registerPhoto(Long userId, MultipartFile mainPhoto, List<MultipartFile> photos) {
        userCRUDService.findById(userId);
        userPhotoService.registerPhoto(userId, mainPhoto, photos);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateNickname(Long userId, NicknameRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateNickname(user, request);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateBirth(Long userId, BirthRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateBirth(user, request);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateGender(Long userId, GenderRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateGender(user, request);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateMbti(Long userId, MbtiRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateMbti(user, request);
        return UserUpdateResponse.from(userId);
    }

//    public UserUpdateResponse updateIncome(Long userId, IncomeRequest request) {
//        User user = userCRUDService.findById(userId);
//        userProfileService.updateIncome(user, request);
//        return UserUpdateResponse.from(userId);
//    }

    public UserUpdateResponse updateAddress(Long userId, AddressRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateAddress(user, request);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateEducation(Long userId, EducationRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateEducation(user, request);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updatePhoneNum(Long userId, PhoneNumRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updatePhoneNum(user, request);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateHopeAge(Long userId, HopeAgeRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateHopeAge(user, request);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateCareer(Long userId, CareerRequest request) {
        User user = userCRUDService.findById(userId);
        if (request.getCareerId() != null && request.getNewCareerName() == null) {
            userCareerService.updateOriginCareer(user, request.getCareerId());
        } else if (request.getNewCareerName() != null && request.getCareerId() == null) {
            userCareerService.updateNewCareer(user, request.getNewCareerName());
        } else {
            throw new UserException(ErrorCode.CAREER_UPDATE_REQUEST_ERROR);
        }
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateHobby(Long userId, HobbyRequest request) {
        userCRUDService.findById(userId);
        return userHobbyService.updateHobby(userId, request);
    }

    public UserUpdateResponse updateLifeStyle(Long userId, LifeStyleRequest request) {
        userCRUDService.findById(userId);
        return userLifeStyleService.updateLifeStyle(userId, request);
    }

    public UserUpdateResponse updateDistance(Long userId, DistanceRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateDistance(user, request);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateWeightValue(Long userId, WeightValueRequest request) {
        userCRUDService.findById(userId);
        userWeightValueService.updateWeightValue(userId, request);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updatePos(Long userId, Double x, Double y) {
        return userPosService.updatePos(userId, x, y);
    }

    public MyInfoResponse getMyInfo(Long userId) {
        User user = userCRUDService.findById(userId);
        String mainPhoto = userPhotoService.getMainPhotoOfUser(userId);
        List<String> photos = userPhotoService.getPhotosOfUser(userId);
        List<String> hobbies = userHobbyService.findHobbiesByUser(userId);
        List<String> lifeStyles = userLifeStyleService.findLifeStylesByUser(userId);
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
}

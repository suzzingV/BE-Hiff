package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.UserPos;
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
import hiff.hiff.behiff.domain.user.presentation.dto.req.JobRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.LifeStyleRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.MbtiRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.NicknameRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.PhoneNumRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.PosRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.WeightValueRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.res.MyInfoResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.TagResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserDetailResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserEvaluatedScoreResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserUpdateResponse;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserHobbyService userHobbyService;
    private final UserLifeStyleService userLifeStyleService;
    private final UserJobService userJobService;
    private final UserWeightValueService userWeightValueService;
    private final UserPhotoService userPhotoService;
    private final UserProfileService userProfileService;
    private final UserCRUDService userCRUDService;
    private final UserPosService userPosService;

    public User registerUser(String email, String socialId, SocialType socialType,
        Role role) {
        User user = userCRUDService.registerUser(email, socialId, socialType, role);
        userWeightValueService.createWeightValue(user.getId());
        return user;
    }

    public UserUpdateResponse createPos(Long userId, PosRequest request) {
        {
            return userPosService.createPos(userId, request);
        }
    }

    public void withdraw(Long userId, Optional<String> accessToken, Optional<String> refreshToken) {
        User user = userCRUDService.findUserById(userId);
        userCRUDService.withdraw(user, accessToken, refreshToken);
    }

//    public UserRegisterResponse registerPhoto(Long userId, MultipartFile mainPhoto, List<MultipartFile> photos) {
//        findUserById(userId);
//        userPhotoService.registerPhoto(mainPhoto, photos);
//        return UserRegisterResponse.from(userId);
//    }

    public UserUpdateResponse updateNickname(Long userId, NicknameRequest request) {
        User user = userCRUDService.findUserById(userId);
        userProfileService.updateNickname(user, request);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateBirth(Long userId, BirthRequest request) {
        User user = userCRUDService.findUserById(userId);
        userProfileService.updateBirth(user, request);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateGender(Long userId, GenderRequest request) {
        User user = userCRUDService.findUserById(userId);
        userProfileService.updateGender(user, request);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateMbti(Long userId, MbtiRequest request) {
        User user = userCRUDService.findUserById(userId);
        userProfileService.updateMbti(user, request);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateIncome(Long userId, IncomeRequest request) {
        User user = userCRUDService.findUserById(userId);
        userProfileService.updateIncome(user, request);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateAddress(Long userId, AddressRequest request) {
        User user = userCRUDService.findUserById(userId);
        userProfileService.updateAddress(user, request);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateEducation(Long userId, EducationRequest request) {
        User user = userCRUDService.findUserById(userId);
        userProfileService.updateEducation(user, request);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updatePhoneNum(Long userId, PhoneNumRequest request) {
        User user = userCRUDService.findUserById(userId);
        userProfileService.updatePhoneNum(user, request);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateHopeAge(Long userId, HopeAgeRequest request) {
        User user = userCRUDService.findUserById(userId);
        userProfileService.updateHopeAge(user, request);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateJob(Long userId, JobRequest request) {
        User user = userCRUDService.findUserById(userId);
        if (request.getJobId() != null && request.getNewJobName() == null) {
            userJobService.updateOriginJob(user, request.getJobId());
        } else if (request.getNewJobName() != null && request.getJobId() == null) {
            userJobService.updateNewJob(user, request.getNewJobName());
        } else {
            throw new UserException(ErrorCode.JOB_UPDATE_REQUEST_ERROR);
        }
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateHobby(Long userId, HobbyRequest request) {
        userCRUDService.findUserById(userId);
        return userHobbyService.updateHobby(userId, request);
    }

    public UserUpdateResponse updateLifeStyle(Long userId, LifeStyleRequest request) {
        userCRUDService.findUserById(userId);
        return userLifeStyleService.updateLifeStyle(userId, request);
    }

    public UserUpdateResponse updateDistance(Long userId, DistanceRequest request) {
        User user = userCRUDService.findUserById(userId);
        userProfileService.updateDistance(user, request);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateWeightValue(Long userId, WeightValueRequest request) {
        userCRUDService.findUserById(userId);
        userWeightValueService.updateWeightValue(userId, request);
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updatePos(Long userId, PosRequest request) {
        {
            return userPosService.updatePos(userId, request);
        }
    }

    public MyInfoResponse getMyInfo(Long userId) {
        User user = userCRUDService.findUserById(userId);
        List<String> photos = userPhotoService.getPhotosOfUser(userId);
        List<String> hobbies = userHobbyService.getHobbiesOfUser(userId);
        List<String> lifeStyles = userLifeStyleService.getLifeStylesOfUser(userId);
        WeightValue weightValue = userWeightValueService.findByUserId(userId);

        return MyInfoResponse.of(user, hobbies, photos, lifeStyles, weightValue);
    }

    public UserDetailResponse getUserDetail(Long myId, Long userId) {
        User user = userCRUDService.findUserById(userId);
        List<String> photos = userPhotoService.getPhotosOfUser(userId);
        List<String> hobbies = userHobbyService.getHobbiesOfUser(userId);
        List<String> lifeStyles = userLifeStyleService.getLifeStylesOfUser(userId);
        Optional<UserPos> userPos = userProfileService.findUserPosByUserId(userId);
        String posX = userPos.map(UserPos::getX).orElse(null);
        String posY = userPos.map(UserPos::getY).orElse(null);

        return UserDetailResponse.of(user, photos, hobbies, lifeStyles, posX, posY);
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
    public List<TagResponse> getJobs() {
        return userJobService.getAllJobs()
            .stream().map(job ->
                TagResponse.builder()
                    .id(job.getId())
                    .name(job.getName())
                    .build())
            .toList();
    }

    public UserEvaluatedScoreResponse getEvaluatedScore(Long userId) {
        User user = userCRUDService.findUserById(userId);
        Double score = userProfileService.getEvaluatedScore(user);
        return UserEvaluatedScoreResponse.builder()
            .evaluatedScore(score)
            .userId(userId)
            .build();
    }
}

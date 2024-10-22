package hiff.hiff.behiff.domain.user.application.service;

import static hiff.hiff.behiff.domain.matching.application.service.HiffMatchingService.HIFF_MATCHING_PREFIX;
import static hiff.hiff.behiff.domain.matching.application.service.MatchingService.MATCHING_DURATION;
import static hiff.hiff.behiff.domain.matching.util.Calculator.computeTotalScoreByMatcher;
import static hiff.hiff.behiff.global.util.DateCalculator.getTodayDate;

import hiff.hiff.behiff.domain.matching.application.dto.MatchingInfoDto;
import hiff.hiff.behiff.domain.matching.application.service.HiffMatchingService;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingSimpleResponse;
import hiff.hiff.behiff.domain.matching.util.SimilarityFactory;
import hiff.hiff.behiff.domain.user.application.dto.UserIntroductionDto;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.UserCareer;
import hiff.hiff.behiff.domain.user.domain.entity.UserGrad;
import hiff.hiff.behiff.domain.user.domain.entity.UserHobby;
import hiff.hiff.behiff.domain.user.domain.entity.UserIncome;
import hiff.hiff.behiff.domain.user.domain.entity.UserLifeStyle;
import hiff.hiff.behiff.domain.user.domain.entity.UserUniversity;
import hiff.hiff.behiff.domain.user.domain.entity.WeightValue;
import hiff.hiff.behiff.domain.user.domain.enums.Role;
import hiff.hiff.behiff.domain.user.infrastructure.UserUniversityRepository;
import hiff.hiff.behiff.domain.user.presentation.dto.req.BirthRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.BodyTypeRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.BuddyRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.ConflictResolutionRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.ContactFrequencyRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.DistanceRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.DrinkingRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.EducationRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.FashionRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.GenderRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.HeightRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.HobbyRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.HopeAgeRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.IdeologyRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.IntroductionRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.LifeStyleRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.MbtiRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.NicknameRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.ReligionRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.SchoolRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.SignedUrlRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.SmokingRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.UserCareerRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.UserIncomeRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.UserPhotoRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.UserQuestionRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.UserSchoolRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.WeightValueRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.res.SignedUrlResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserInfoResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserUpdateResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserWeightValueResponse;
import hiff.hiff.behiff.global.common.redis.RedisService;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceFacade {

    private final UserHobbyService userHobbyService;
    private final UserLifeStyleService userLifeStyleService;
    private final UserCareerService userCareerService;
    private final UserWeightValueService userWeightValueService;
    private final UserPhotoService userPhotoService;
    private final UserProfileService userProfileService;
    private final UserCRUDService userCRUDService;
    private final UserPosService userPosService;
    private final UserFashionService userFashionService;
    private final UserIntroductionService userIntroductionService;
    private final UserSchoolService userSchoolService;
    private final UserIncomeService userIncomeService;
    private final HiffMatchingService hiffMatchingService;
    private final SimilarityFactory similarityFactory;
    private final RedisService redisService;

    public User registerUser(
        Role role, String phoneNum, Double lat, Double lon) {
        User user = userCRUDService.registerUser(role, phoneNum);
        userWeightValueService.createWeightValue(user.getId());
        userPosService.createPos(user.getId(), lat, lon);
        return user;
    }

    public User findById(Long userId) {
        return userCRUDService.findById(userId);
    }

    public UserUpdateResponse updatePhotos(Long userId, UserPhotoRequest request) {
        userCRUDService.findById(userId);
        userPhotoService.registerMainPhoto(userId, request.getNewMainPhoto());
        userPhotoService.registerPhotos(userId, request.getNewPhotos());
        userPhotoService.deletePhotos(request.getTrashPhotos());
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateNickname(Long userId, NicknameRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateNickname(user, request.getNickname());
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateBirth(Long userId, BirthRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateBirth(user, request.getBirthYear(), request.getBirthMonth(),
            request.getBirthDay());
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

    public UserUpdateResponse updateCareer(Long userId, UserCareerRequest request) {
        userCRUDService.findById(userId);
        userCareerService.updateCareer(userId, request.getField(), request.getCompany(), request.getVerification());
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
        User user = userCRUDService.findById(userId);
        userWeightValueService.updateWeightValue(userId, request.getAppearance(),
            request.getHobby(), request.getLifeStyle(), request.getMbti());
        List<UserHobby> matcherHobbies = userHobbyService.findByUserId(userId);
        List<UserLifeStyle> matcherLifeStyles = userLifeStyleService.findByUserId(userId);
        WeightValue matcherWV = userWeightValueService.findByUserId(userId);
        List<MatchingSimpleResponse> matchings = hiffMatchingService.getMatchings(userId);
        if (!matchings.isEmpty()) {
            MatchingSimpleResponse matchedResponse = matchings.get(0);
            User matched = userCRUDService.findById(matchedResponse.getUserId());
            WeightValue matchedWV = userWeightValueService.findByUserId(
                matchedResponse.getUserId());
            List<UserHobby> matchedHobbies = userHobbyService.findByUserId(
                matchedResponse.getUserId());
            List<UserLifeStyle> matchedLifeStyle = userLifeStyleService.findByUserId(
                matchedResponse.getUserId());
            MatchingInfoDto userMatchingInfo = getNewMatchingInfo(user, matched, matcherWV,
                matcherHobbies, matchedHobbies, matcherLifeStyles, matchedLifeStyle);
            MatchingInfoDto matchedMatchingInfo = getNewMatchingInfo(matched, user, matchedWV,
                matchedHobbies, matcherHobbies, matchedLifeStyle, matcherLifeStyles);
            String today = getTodayDate();
            cachMatchingScore(userId, matched.getId(), userMatchingInfo,
                matchedMatchingInfo.getTotalScoreByMatcher(), today, MATCHING_DURATION);
        }
        return UserUpdateResponse.from(userId);
    }

    private void cachMatchingScore(Long matcherId, Long matchedId, MatchingInfoDto matchingInfoDto,
        int matchedTotalScore, String date, Duration duration) {
//        String prefix = date + HIFF_MATCHING_PREFIX;
        String key = HIFF_MATCHING_PREFIX + matcherId + "_" + matchedId;
        String value = matchingInfoDto.getTotalScoreByMatcher() + "/" + matchedTotalScore + "/"
            + matchingInfoDto.getMbtiSimilarity() + "/"
            + matchingInfoDto.getHobbySimilarity() + "/"
            + matchingInfoDto.getLifeStyleSimilarity();
        redisService.setValue(key, value, duration);

        key = HIFF_MATCHING_PREFIX + matchedId + "_" + matcherId;
        value = matchedTotalScore + "/" + matchingInfoDto.getTotalScoreByMatcher() + "/" +
            +matchingInfoDto.getMbtiSimilarity() + "/"
            + matchingInfoDto.getHobbySimilarity() + "/"
            + matchingInfoDto.getLifeStyleSimilarity();
        redisService.setValue(key, value, duration);
    }

    protected MatchingInfoDto getNewMatchingInfo(User matcher, User matched,
        WeightValue matcherWV, List<UserHobby> matcherHobbies, List<UserHobby> matchedHobbies,
        List<UserLifeStyle> matcherLifeStyles, List<UserLifeStyle> matchedLifeStyles) {
        int mbtiSimilarity = similarityFactory.getMbtiSimilarity(matcher, matched);
        int hobbySimilarity = similarityFactory.getHobbySimilarity(matcherHobbies, matchedHobbies);
        int lifeStyleSimilarity = similarityFactory.getLifeStyleSimilarity(matcherLifeStyles,
            matchedLifeStyles);
//        int incomeSimilarity = similarityFactory.getIncomeSimilarity(matcher, matched);
        Integer totalScore = computeTotalScoreByMatcher(matcherWV, mbtiSimilarity, hobbySimilarity,
            lifeStyleSimilarity, matched.getEvaluatedScore());
        return MatchingInfoDto.builder()
            .mbtiSimilarity(mbtiSimilarity)
            .hobbySimilarity(hobbySimilarity)
            .lifeStyleSimilarity(lifeStyleSimilarity)
//            .incomeSimilarity(incomeSimilarity)
            .totalScoreByMatcher(totalScore)
            .build();
    }

    public UserUpdateResponse updatePos(Long userId, Double x, Double y) {
        return userPosService.updatePos(userId, x, y);
    }

    public UserInfoResponse getUserInfo(Long userId) {
        User user = userCRUDService.findById(userId);
        String mainPhoto = user.getMainPhoto();
        List<String> photos = userPhotoService.getPhotosOfUser(userId);
        List<String> hobbies = userHobbyService.findNameByUser(userId);
        List<String> lifeStyles = userLifeStyleService.findNamesByUser(userId);
        WeightValue weightValue = userWeightValueService.findByUserId(userId);
        List<String> fashions = userFashionService.findNameByUser(userId);
        List<UserIntroductionDto> introductions = userIntroductionService.findIntroductionByUserId(
            userId);
        UserCareer userCareer = userCareerService.findByUserId(userId);
        UserUniversity userUniversity = userSchoolService.findByUniversityUserId(userId);
        UserGrad userGrad = userSchoolService.findByGradUserId(userId);
        UserIncome userIncome = userIncomeService.findByUserId(userId);

        return UserInfoResponse.of(user, hobbies, mainPhoto, photos, lifeStyles, weightValue,
            fashions, introductions, userCareer.getField(), userUniversity.getName(), userGrad.getName(), userIncome.getIncome());
    }

    public UserWeightValueResponse getWeightValue(Long userId) {
        WeightValue wv = userWeightValueService.findByUserId(userId);
        User user = userCRUDService.findById(userId);
        return UserWeightValueResponse.of(userId, wv.getAppearance(), wv.getHobby(),
            wv.getLifeStyle(), wv.getMbti(), user.getHopeMinAge(), user.getHopeMaxAge(),
            user.getMinDistance(), user.getMaxDistance());
    }

    public UserUpdateResponse updateSmokingStatus(Long userId, SmokingRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateSmokingStatus(user, request.getIsSmoking());
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateDrinkingStatus(Long userId, DrinkingRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateDrinkingStatus(user, request.getDrinking());
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateBuddy(Long userId, BuddyRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateBuddy(user, request.getBuddy());
        return UserUpdateResponse.from(user.getId());
    }

    public UserUpdateResponse updateReligion(Long userId, ReligionRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateReligion(user, request.getReligion());
        return UserUpdateResponse.from(user.getId());
    }

    public UserUpdateResponse updateIdeology(Long userId, IdeologyRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateIdeology(user, request.getIdeology());
        return UserUpdateResponse.from(user.getId());
    }

    public UserUpdateResponse updateContactFrequency(Long userId, ContactFrequencyRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateContactFrequency(user, request.getContactFrequency());
        return UserUpdateResponse.from(user.getId());
    }

    public UserUpdateResponse updateConflictResolution(Long userId,
        ConflictResolutionRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateConflictResolution(user, request.getConflictResolution());
        return UserUpdateResponse.from(user.getId());
    }

    public UserUpdateResponse updateHeight(Long userId, HeightRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateHeight(user, request.getHeight());
        return UserUpdateResponse.from(user.getId());
    }

    public UserUpdateResponse updateBodyType(Long userId, BodyTypeRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateBodyType(user, request.getBodyType());
        return UserUpdateResponse.from(user.getId());
    }

    public UserUpdateResponse updateFashion(Long userId, FashionRequest request) {
        userCRUDService.findById(userId);
        userFashionService.updateFashion(userId, request.getFashions());
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateIntroduction(Long userId, IntroductionRequest request) {
        userIntroductionService.updateIntroduction(userId, request.getQuestionId(),
            request.getContent());
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateUserQuestion(Long userId, UserQuestionRequest request) {
        userCRUDService.findById(userId);
        userIntroductionService.registerUserIntroduction(userId, request);
        return UserUpdateResponse.from(userId);
    }

    public SignedUrlResponse generateSingedUrl(SignedUrlRequest request) {
        return userPhotoService.generateSingedUrl(request.getMainPhotoName(),
            request.getPhotoNames());
    }

    public UserUpdateResponse createUniversity(Long userId, UserSchoolRequest request) {
        userSchoolService.createUniversity(userId, request.getName(), request.getVerification());
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse createGrad(Long userId, UserSchoolRequest request) {
        userSchoolService.createGrad(userId, request.getName(), request.getVerification());
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse createIncome(Long userId, UserIncomeRequest request) {
        userIncomeService.createIncome(userId, request.getIncome(), request.getVerification());
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse createCareer(Long userId, UserCareerRequest request) {
        userCareerService.createCareer(userId, request.getCompany(), request.getField(), request.getVerification());
        return UserUpdateResponse.from(userId);
    }
}

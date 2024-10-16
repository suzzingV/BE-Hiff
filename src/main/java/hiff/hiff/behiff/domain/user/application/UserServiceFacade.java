package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.matching.application.dto.MatchingInfoDto;
import hiff.hiff.behiff.domain.matching.application.service.HiffMatchingService;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingSimpleResponse;
import hiff.hiff.behiff.domain.matching.util.SimilarityFactory;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.UserHobby;
import hiff.hiff.behiff.domain.user.domain.entity.UserLifeStyle;
import hiff.hiff.behiff.domain.user.domain.entity.WeightValue;
import hiff.hiff.behiff.domain.user.domain.enums.Role;
import hiff.hiff.behiff.domain.user.presentation.dto.req.*;
import hiff.hiff.behiff.domain.user.presentation.dto.res.*;

import java.time.Duration;
import java.util.List;

import hiff.hiff.behiff.global.common.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static hiff.hiff.behiff.domain.matching.application.service.HiffMatchingService.HIFF_MATCHING_PREFIX;
import static hiff.hiff.behiff.domain.matching.application.service.MatchingService.MATCHING_DURATION;
import static hiff.hiff.behiff.domain.matching.util.Calculator.computeTotalScoreByMatcher;
import static hiff.hiff.behiff.global.util.DateCalculator.getTodayDate;

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

    public UserUpdateResponse updatePhotos(Long userId, MultipartFile mainPhoto,
        List<MultipartFile> photos, UserPhotoRequest request) {
        userCRUDService.findById(userId);
        userPhotoService.registerMainPhoto(userId, mainPhoto);
        userPhotoService.registerPhotos(userId, photos);
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
        User user = userCRUDService.findById(userId);
        userWeightValueService.updateWeightValue(userId, request.getAppearance(), request.getHobby(), request.getLifeStyle(), request.getMbti());
        List<UserHobby> matcherHobbies = userHobbyService.findByUserId(userId);
        List<UserLifeStyle> matcherLifeStyles = userLifeStyleService.findByUserId(userId);
        WeightValue matcherWV = userWeightValueService.findByUserId(userId);
        List<MatchingSimpleResponse> matchings = hiffMatchingService.getMatchings(userId);
        if(!matchings.isEmpty()) {
            MatchingSimpleResponse matchedResponse = matchings.get(0);
            User matched = userCRUDService.findById(matchedResponse.getUserId());
            WeightValue matchedWV = userWeightValueService.findByUserId(matchedResponse.getUserId());
            List<UserHobby> matchedHobbies = userHobbyService.findByUserId(matchedResponse.getUserId());
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

        return UserInfoResponse.of(user, hobbies, mainPhoto, photos, lifeStyles, weightValue);
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

    public UserWeightValueResponse getWeightValue(Long userId) {
        WeightValue wv = userWeightValueService.findByUserId(userId);
        User user = userCRUDService.findById(userId);
        return UserWeightValueResponse.of(userId, wv.getAppearance(), wv.getHobby(), wv.getLifeStyle(), wv.getMbti(), user.getHopeMinAge(), user.getHopeMaxAge(), user.getMinDistance(), user.getMaxDistance());
    }

    public UserUpdateResponse updateSmokingStatus(Long userId, SmokingRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateSmokingStatus(user, request.getIsSmoking());
        return UserUpdateResponse.from(userId);
    }

    public UserUpdateResponse updateDrinkingStatus(Long userId, DrinkingRequest request) {
        User user = userCRUDService.findById(userId);
        userProfileService.updateDrinkingStatus(user, request.getIsDrinking());
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
}

package hiff.hiff.behiff.domain.user.application.service;

import hiff.hiff.behiff.domain.plan.application.service.PlanService;
import hiff.hiff.behiff.domain.profile.application.service.UserIntroductionService;
import hiff.hiff.behiff.domain.profile.application.service.UserPhotoService;
import hiff.hiff.behiff.domain.profile.application.service.UserPosService;
import hiff.hiff.behiff.domain.profile.application.service.UserProfileService;
import hiff.hiff.behiff.domain.profile.domain.entity.UserProfile;
import hiff.hiff.behiff.domain.profile.application.dto.UserIntroductionDto;
import hiff.hiff.behiff.domain.profile.presentation.dto.res.ProfileUpdateResponse;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.enums.Role;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserInfoResponse;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserPhotoService userPhotoService;
    private final UserPosService userPosService;
    private final UserIntroductionService userIntroductionService;
    private final PlanService planService;
    private final UserProfileService userProfileService;
    private final UserRepository userRepository;

    public User registerUser(
        Role role, String phoneNum, Double lat, Double lon) {
        User user = createUser(role, phoneNum);
        userPosService.createPos(user.getId(), lat, lon);
        planService.createUserPlan(user.getId());
        userProfileService.createUserProfile(user.getId());
        return user;
    }

    public UserInfoResponse getUserInfo(Long userId) {
        UserProfile userProfile = userProfileService.findByUserId(userId);
        List<String> photos = userPhotoService.getPhotosOfUser(userId);
//        List<String> hobbies = userHobbyService.findNameByUser(userId);
//        List<String> lifeStyles = userLifeStyleService.findNamesByUser(userId);
//        UserWeighting weightValue = userWeightValueService.findByUserId(userId);
//        List<String> fashions = userFashionService.findNameByUser(userId);
        List<UserIntroductionDto> introductions = userIntroductionService.findIntroductionByUserId(
            userId);
//        UserCareer userCareer = userCareerService.findByUserId(userId);
//        UserUniversity userUniversity = userSchoolService.findByUniversityUserId(userId);
//        UserGrad userGrad = userSchoolService.findByGradUserId(userId);
//        UserIncome userIncome = userIncomeService.findByUserId(userId);

        return UserInfoResponse.of(userProfile, photos, introductions);
    }

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    public ProfileUpdateResponse updatePhoneNum(Long userId, String phoneNum) {
        User user = findById(userId);
        user.changePhoneNum(phoneNum);
        return ProfileUpdateResponse.from(userId);
    }

    private User createUser(Role role, String phoneNum) {
        User user = User.builder()
            .phoneNum(phoneNum)
            .role(role)
            .build();
        return userRepository.save(user);
    }

//    public UserUpdateResponse updateWeightValue(Long userId, WeightValueRequest request) {
//        User user = userCRUDService.findById(userId);
//        userWeightValueService.updateWeightValue(userId, request.getAppearance(),
//            request.getHobby(), request.getLifeStyle(), request.getMbti());
//        List<UserHobby> matcherHobbies = userHobbyService.findByUserId(userId);
//        List<UserLifeStyle> matcherLifeStyles = userLifeStyleService.findByUserId(userId);
//        Weighting matcherWV = userWeightValueService.findByUserId(userId);
//        List<MatchingSimpleResponse> matchings = hiffMatchingService.getMatchings(userId);
//        if (!matchings.isEmpty()) {
//            MatchingSimpleResponse matchedResponse = matchings.get(0);
//            User matched = userCRUDService.findById(matchedResponse.getUserId());
//            Weighting matchedWV = userWeightValueService.findByUserId(
//                matchedResponse.getUserId());
//            List<UserHobby> matchedHobbies = userHobbyService.findByUserId(
//                matchedResponse.getUserId());
//            List<UserLifeStyle> matchedLifeStyle = userLifeStyleService.findByUserId(
//                matchedResponse.getUserId());
//            MatchingInfoDto userMatchingInfo = getNewMatchingInfo(user, matched, matcherWV,
//                matcherHobbies, matchedHobbies, matcherLifeStyles, matchedLifeStyle);
//            MatchingInfoDto matchedMatchingInfo = getNewMatchingInfo(matched, user, matchedWV,
//                matchedHobbies, matcherHobbies, matchedLifeStyle, matcherLifeStyles);
//            String today = getTodayDate();
//            cachMatchingScore(userId, matched.getId(), userMatchingInfo,
//                matchedMatchingInfo.getTotalScoreByMatcher(), today, MATCHING_DURATION);
//        }
//        return UserUpdateResponse.from(userId);
//    }

//    private void cachMatchingScore(Long matcherId, Long matchedId, MatchingInfoDto matchingInfoDto,
//        int matchedTotalScore, String date, Duration duration) {
////        String prefix = date + HIFF_MATCHING_PREFIX;
//        String key = HIFF_MATCHING_PREFIX + matcherId + "_" + matchedId;
//        String value = matchingInfoDto.getTotalScoreByMatcher() + "/" + matchedTotalScore + "/"
//            + matchingInfoDto.getMbtiSimilarity() + "/"
//            + matchingInfoDto.getHobbySimilarity() + "/"
//            + matchingInfoDto.getLifeStyleSimilarity();
//        redisService.setValue(key, value, duration);
//
//        key = HIFF_MATCHING_PREFIX + matchedId + "_" + matcherId;
//        value = matchedTotalScore + "/" + matchingInfoDto.getTotalScoreByMatcher() + "/" +
//            +matchingInfoDto.getMbtiSimilarity() + "/"
//            + matchingInfoDto.getHobbySimilarity() + "/"
//            + matchingInfoDto.getLifeStyleSimilarity();
//        redisService.setValue(key, value, duration);
//    }

//    protected MatchingInfoDto getNewMatchingInfo(User matcher, User matched,
//        WeightValue matcherWV, List<UserHobby> matcherHobbies, List<UserHobby> matchedHobbies,
//        List<UserLifeStyle> matcherLifeStyles, List<UserLifeStyle> matchedLifeStyles) {
//        int mbtiSimilarity = similarityFactory.getMbtiSimilarity(matcher, matched);
//        int hobbySimilarity = similarityFactory.getHobbySimilarity(matcherHobbies, matchedHobbies);
//        int lifeStyleSimilarity = similarityFactory.getLifeStyleSimilarity(matcherLifeStyles,
//            matchedLifeStyles);
////        int incomeSimilarity = similarityFactory.getIncomeSimilarity(matcher, matched);
//        Integer totalScore = computeTotalScoreByMatcher(matcherWV, mbtiSimilarity, hobbySimilarity,
//            lifeStyleSimilarity, matched.getEvaluatedScore());
//        return MatchingInfoDto.builder()
//            .mbtiSimilarity(mbtiSimilarity)
//            .hobbySimilarity(hobbySimilarity)
//            .lifeStyleSimilarity(lifeStyleSimilarity)
////            .incomeSimilarity(incomeSimilarity)
//            .totalScoreByMatcher(totalScore)
//            .build();
//    }

//    public UserWeightValueResponse getWeightValue(Long userId) {
//        WeightValue wv = userWeightValueService.findByUserId(userId);
//        User user = userCRUDService.findById(userId);
//        return UserWeightValueResponse.of(userId, wv.getAppearance(), wv.getHobby(),
//            wv.getLifeStyle(), wv.getMbti(), user.getHopeMinAge(), user.getHopeMaxAge(),
//            user.getMinDistance(), user.getMaxDistance());
//    }
}

package hiff.hiff.behiff.domain.matching.application.service;

import hiff.hiff.behiff.domain.matching.infrastructure.MatchingRepository;
import hiff.hiff.behiff.domain.matching.util.SimilarityFactory;
import hiff.hiff.behiff.domain.profile.application.service.UserPosService;
import hiff.hiff.behiff.domain.profile.domain.entity.UserProfile;
import hiff.hiff.behiff.domain.profile.infrastructure.UserProfileRepository;
import hiff.hiff.behiff.global.common.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hiff.hiff.behiff.global.util.DateCalculator.getMatchingDate;

@Service
@Transactional
@Slf4j
public class DailyMatchingService extends MatchingService {

//    private final UserService userCRUDService;
//    private final UserRepository userRepository;
    private final RedisService redisService;
//    private final UserPhotoService userPhotoService;
    private final UserProfileRepository userProfileRepository;

//    public static final Duration MATCHING_DURATION = Duration.ofDays(1);
    public static final String MATCHING_PREFIX = "matching_";

    public DailyMatchingService(UserPosService userPosService, RedisService redisService,
        MatchingRepository matchingRepository, SimilarityFactory similarityFactory, UserProfileRepository userProfileRepository) {
        super(userPosService, redisService, matchingRepository, similarityFactory);
//        this.userCRUDService = userService;
//        this.userRepository = userRepository;
        this.redisService = redisService;
        this.userProfileRepository = userProfileRepository;
//        this.userPhotoService = userPhotoService;
    }
//
//    // TODO: 남은 시간
//    // TODO: 외모 점수 없을 때
//    public List<MatchingSimpleResponse> getMatchings(Long userId) {
//        User matcher = userCRUDService.findById(userId);
//        List<String> originalMatching = redisService.scanKeysWithPrefix(
//            DAILY_MATCHING_PREFIX + userId + "_");
//        if (!originalMatching.isEmpty()) {
//            return getCachedMatching(userId, originalMatching);
//        }
//
//        return getNewMatching(matcher);
//    }
//
//    public List<MatchingSimpleResponse> getPaidMatching(Long userId) {
//        User matcher = userCRUDService.findById(userId);
//        List<String> originalMatching = redisService.scanKeysWithPrefix(
//            PAID_DAILY_MATCHING_PREFIX + userId + "_");
//        List<MatchingSimpleResponse> newMatching = getNewMatching(matcher);
//
//        originalMatching.forEach(redisService::delete);
//        return newMatching;
//    }
//
//    public DailyMatchingDetailResponse getMatchingDetails(Long matcherId, Long matchedId) {
//        if (!isMatchedBefore(matcherId, matchedId)) {
//            throw new MatchingException(ErrorCode.MATCHING_HISTORY_NOT_FOUND);
//        }
//        User matcher = userCRUDService.findById(matcherId);
//        User matched = userCRUDService.findById(matchedId);
//
//        String mainPhoto = matched.getMainPhoto();
//        List<String> photos = userPhotoService.getPhotosOfUser(matchedId);
//        List<NameWithCommonDto> hobbies = userHobbyService.getHobbiesWithCommon(matcherId,
//            matchedId);
//        List<NameWithCommonDto> lifeStyles = userLifeStyleService.getLifeStylesWithCommon(matcherId,
//            matchedId);
//        Double distance = getDistance(matcherId, matchedId);
//        MatchingInfoDto matchingInfo = getCachedMatchingInfo(
//            matcherId, matchedId);
//
//        return DailyMatchingDetailResponse.of(matcher, matched, distance, mainPhoto, photos,
//            matchingInfo,
//            hobbies, lifeStyles);
//    }
//
//    private List<MatchingSimpleResponse> getCachedMatching(Long userId,
//        List<String> originalMatching) {
//        return originalMatching.stream()
//            .map(key -> {
//                Long matchedId = getMatchedIdFromKey(key);
//                User matched = userCRUDService.findById(matchedId);
//                MatchingInfoDto matchingInfo = getCachedMatchingInfo(userId,
//                    matchedId);
//
//                return MatchingSimpleResponse.builder()
//                    .userId(matchedId)
//                    .age(matched.getAge())
//                    .nickname(matched.getNickname())
//                    .mainPhoto(matched.getMainPhoto())
//                    .matcherTotalScore(matchingInfo.getTotalScoreByMatcher())
//                    .distance(getDistance(userId, matchedId))
//                    .build();
//            }).toList();
//    }
//
    public void performMatching(UserProfile matcher) {
        userProfileRepository.getRandomMatched(matcher.getId(),
                matcher.getGender(), matcher.getLookScore())
            .forEach(matched -> {
//                Weighting matcherWV = userWeightValueService.findByUserId(matcher.getId());
//                List<UserHobby> matcherHobbies = userHobbyService.findByUserId(matcher.getId());
//                List<UserHobby> matchedHobbies = userHobbyService.findByUserId(matched.getId());
//                List<UserLifeStyle> matcherLifeStyles = userLifeStyleService.findByUserId(
//                    matcher.getId());
//                List<UserLifeStyle> matchedLifeStyles = userLifeStyleService.findByUserId(
//                    matched.getId());
//                MatchingInfoDto matchingInfoDto = getNewMatchingInfo(matcher, matched, matcherWV,
//                    matcherHobbies, matchedHobbies, matcherLifeStyles, matchedLifeStyles);

                cachMatchingScore(matcher, matched);
                recordMatchingHistory(matcher.getUserId(), matched.getUserId());
            });
    }
//
//    private MatchingInfoDto getCachedMatchingInfo(Long matcherId, Long matchedId) {
//        String value = getCachedValue(matcherId, matchedId, DAILY_MATCHING_PREFIX);
//
//        StringTokenizer st = new StringTokenizer(value, "/");
//        int totalScore = Integer.parseInt(st.nextToken());
//        int mbtiSimilarity = Integer.parseInt(st.nextToken());
//        int hobbySimilarity = Integer.parseInt(st.nextToken());
//        int lifeStyleSimilarity = Integer.parseInt(st.nextToken());
////        int incomeSimilarity = Integer.parseInt(st.nextToken());
//
//        return MatchingInfoDto.builder()
//            .matcherId(matcherId)
//            .matchedId(matchedId)
//            .totalScoreByMatcher(totalScore)
//            .mbtiSimilarity(mbtiSimilarity)
//            .hobbySimilarity(hobbySimilarity)
////            .incomeSimilarity(incomeSimilarity)
//            .lifeStyleSimilarity(lifeStyleSimilarity)
//            .build();
//    }
//
    private void cachMatchingScore(UserProfile matcher, UserProfile matched) {
        String matchingDate = getMatchingDate();
        String prefix = MATCHING_PREFIX + matchingDate;
        String key = prefix + "_" + matcher.getUserId();
        Long value = matched.getUserId();
        redisService.setValue(key, value);
    }
}

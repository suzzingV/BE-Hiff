package hiff.hiff.behiff.domain.matching.application.service;

import hiff.hiff.behiff.domain.matching.application.dto.MatchingDataDto;
import hiff.hiff.behiff.domain.matching.infrastructure.MatchingRepository;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingSimpleResponse;
import hiff.hiff.behiff.domain.matching.util.SimilarityFactory;
import hiff.hiff.behiff.domain.profile.application.service.UserPhotoService;
import hiff.hiff.behiff.domain.profile.application.service.UserPosService;
import hiff.hiff.behiff.domain.profile.application.service.UserProfileService;
import hiff.hiff.behiff.domain.profile.domain.entity.UserProfile;
import hiff.hiff.behiff.domain.profile.infrastructure.UserProfileRepository;
import hiff.hiff.behiff.global.common.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static hiff.hiff.behiff.global.util.DateCalculator.TODAY_DATE;
import static hiff.hiff.behiff.global.util.DateCalculator.getMatchingDate;

@Service
@Transactional
@Slf4j
public class DailyMatchingService extends MatchingService {

//    private final UserService userCRUDService;
//    private final UserRepository userRepository;
    private final RedisService redisService;
    private final UserPhotoService userPhotoService;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileService userProfileService;

//    public static final Duration MATCHING_DURATION = Duration.ofDays(1);
    public static final String MATCHING_PREFIX = "matching_";

    public DailyMatchingService(UserPosService userPosService, RedisService redisService,
                                MatchingRepository matchingRepository, SimilarityFactory similarityFactory, UserProfileRepository userProfileRepository, UserProfileService userProfileService, UserPhotoService userPhotoService) {
        super(userPosService, redisService, matchingRepository, similarityFactory);
//        this.userCRUDService = userService;
//        this.userRepository = userRepository;
        this.redisService = redisService;
        this.userProfileRepository = userProfileRepository;
        this.userProfileService = userProfileService;
        this.userPhotoService = userPhotoService;
    }

    public List<MatchingSimpleResponse> getMatchings(Long userId) {
        return redisService.scanKeysWithPrefix(
            MATCHING_PREFIX + TODAY_DATE + "??_" + userId)
                .stream().map(key -> {
                    Long matchedId = redisService.getLongValue(key);
                    UserProfile matched = userProfileService.findByUserId(matchedId);

                    return MatchingSimpleResponse.builder()
                            .age(matched.getAge())
                            .userId(matchedId)
                            .mainPhoto(matched.getMainPhoto())
                            .nickname(matched.getNickname())
                            .location(matched.getLocation())
                            .build();
                }).toList();
    }
//
//    public List<MatchingSimpleResponse> getPaidMatching(Long userId) {
//        User matcher = userCRUDService.findById(userId);
//        List<String> matchingData = redisService.scanKeysWithPrefix(
//            PAID_DAILY_MATCHING_PREFIX + userId + "_");
//        List<MatchingSimpleResponse> newMatching = getNewMatching(matcher);
//
//        matchingData.forEach(redisService::delete);
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
//    private List<MatchingSimpleResponse> getSimpleMatchingInfo(Long userId,
//                                                               List<Long> matchedIds) {
//        return matchedIds.stream()
//            .map(matchedId -> {
////                Long matchedId = getMatchedIdFromKey(key);
//                UserProfile matched = userProfileService.findByUserId(matchedId);
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

    private void cachMatchingScore(UserProfile matcher, UserProfile matched) {
        String matchingDate = getMatchingDate();
        String prefix = MATCHING_PREFIX + matchingDate;
        String key = prefix + "_" + matcher.getUserId();
        Long value = matched.getUserId();
        redisService.setValue(key, value);
    }
}

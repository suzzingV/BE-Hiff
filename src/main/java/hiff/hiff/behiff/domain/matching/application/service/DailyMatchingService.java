package hiff.hiff.behiff.domain.matching.application.service;

import static hiff.hiff.behiff.global.util.DateCalculator.getTodayDate;

import hiff.hiff.behiff.domain.matching.application.dto.MatchingInfoDto;
import hiff.hiff.behiff.domain.matching.application.dto.NameWithCommonDto;
import hiff.hiff.behiff.domain.matching.exception.MatchingException;
import hiff.hiff.behiff.domain.matching.infrastructure.MatchingRepository;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.DailyMatchingDetailResponse;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingSimpleResponse;
import hiff.hiff.behiff.domain.matching.util.SimilarityFactory;
import hiff.hiff.behiff.domain.user.application.service.UserCRUDService;
import hiff.hiff.behiff.domain.user.application.service.UserHobbyService;
import hiff.hiff.behiff.domain.user.application.service.UserLifeStyleService;
import hiff.hiff.behiff.domain.user.application.service.UserPhotoService;
import hiff.hiff.behiff.domain.user.application.service.UserPosService;
import hiff.hiff.behiff.domain.user.application.service.UserWeightValueService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.UserHobby;
import hiff.hiff.behiff.domain.user.domain.entity.UserLifeStyle;
import hiff.hiff.behiff.domain.user.domain.entity.WeightValue;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.time.Duration;
import java.util.List;
import java.util.StringTokenizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class DailyMatchingService extends MatchingService {

    private final UserCRUDService userCRUDService;
    private final UserWeightValueService userWeightValueService;
    private final UserRepository userRepository;
    private final RedisService redisService;
    private final UserHobbyService userHobbyService;
    private final UserLifeStyleService userLifeStyleService;
    private final UserPhotoService userPhotoService;

    private static final Integer DAILY_MATCHING_HEART = 1;
    public static final Duration MATCHING_DURATION = Duration.ofDays(1);
    public static final String DAILY_MATCHING_PREFIX = "daily_";
    public static final String PAID_DAILY_MATCHING_PREFIX = "paidDaily_";

    public DailyMatchingService(UserPosService userPosService, RedisService redisService,
        MatchingRepository matchingRepository, SimilarityFactory similarityFactory,
        UserCRUDService userCRUDService, UserWeightValueService userWeightValueService,
        UserRepository userRepository, RedisService redisService1,
        UserHobbyService userHobbyService, UserLifeStyleService userLifeStyleService,
        UserPhotoService userPhotoService) {
        super(userPosService, redisService, matchingRepository, similarityFactory);
        this.userCRUDService = userCRUDService;
        this.userWeightValueService = userWeightValueService;
        this.userRepository = userRepository;
        this.redisService = redisService1;
        this.userHobbyService = userHobbyService;
        this.userLifeStyleService = userLifeStyleService;
        this.userPhotoService = userPhotoService;
    }

    // TODO: 남은 시간
    // TODO: 외모 점수 없을 때
    public List<MatchingSimpleResponse> getMatchings(Long userId) {
        User matcher = userCRUDService.findById(userId);
        List<String> originalMatching = redisService.scanKeysWithPrefix(
            DAILY_MATCHING_PREFIX + userId + "_");
        if (!originalMatching.isEmpty()) {
            return getCachedMatching(userId, originalMatching);
        }

        return getNewMatching(matcher);
    }

    public List<MatchingSimpleResponse> getPaidMatching(Long userId) {
        User matcher = userCRUDService.findById(userId);
        useHeart(matcher, DAILY_MATCHING_HEART);
        List<String> originalMatching = redisService.scanKeysWithPrefix(
            PAID_DAILY_MATCHING_PREFIX + userId + "_");
        List<MatchingSimpleResponse> newMatching = getNewMatching(matcher);

        originalMatching.forEach(redisService::delete);
        return newMatching;
    }

    public DailyMatchingDetailResponse getMatchingDetails(Long matcherId, Long matchedId) {
        if (!isMatchedBefore(matcherId, matchedId)) {
            throw new MatchingException(ErrorCode.MATCHING_HISTORY_NOT_FOUND);
        }
        User matcher = userCRUDService.findById(matcherId);
        User matched = userCRUDService.findById(matchedId);

        String mainPhoto = matched.getMainPhoto();
        List<String> photos = userPhotoService.getPhotosOfUser(matchedId);
        List<NameWithCommonDto> hobbies = userHobbyService.getHobbiesWithCommon(matcherId,
            matchedId);
        List<NameWithCommonDto> lifeStyles = userLifeStyleService.getLifeStylesWithCommon(matcherId,
            matchedId);
        Double distance = getDistance(matcherId, matchedId);
        MatchingInfoDto matchingInfo = getCachedMatchingInfo(
            matcherId, matchedId);

        return DailyMatchingDetailResponse.of(matcher, matched, distance, mainPhoto, photos,
            matchingInfo,
            hobbies, lifeStyles);
    }

    private List<MatchingSimpleResponse> getCachedMatching(Long userId,
        List<String> originalMatching) {
        return originalMatching.stream()
            .map(key -> {
                Long matchedId = getMatchedIdFromKey(key);
                User matched = userCRUDService.findById(matchedId);
                MatchingInfoDto matchingInfo = getCachedMatchingInfo(userId,
                    matchedId);

                return MatchingSimpleResponse.builder()
                    .userId(matchedId)
                    .age(matched.getAge())
                    .nickname(matched.getNickname())
                    .mainPhoto(matched.getMainPhoto())
                    .matcherTotalScore(matchingInfo.getTotalScoreByMatcher())
                    .distance(getDistance(userId, matchedId))
                    .build();
            }).toList();
    }

    private List<MatchingSimpleResponse> getNewMatching(User matcher) {
        List<MatchingSimpleResponse> responses = userRepository.getDailyMatched(matcher.getId(),
                matcher.getGender())
            .stream()
            .map(matched -> {
                WeightValue matcherWV = userWeightValueService.findByUserId(matcher.getId());
                List<UserHobby> matcherHobbies = userHobbyService.findByUserId(matcher.getId());
                List<UserHobby> matchedHobbies = userHobbyService.findByUserId(matched.getId());
                List<UserLifeStyle> matcherLifeStyles = userLifeStyleService.findByUserId(
                    matcher.getId());
                List<UserLifeStyle> matchedLifeStyles = userLifeStyleService.findByUserId(
                    matched.getId());
                MatchingInfoDto matchingInfoDto = getNewMatchingInfo(matcher, matched, matcherWV,
                    matcherHobbies, matchedHobbies, matcherLifeStyles, matchedLifeStyles);

                cachMatchingScore(matcher, matched, matchingInfoDto);
                recordMatchingHistory(matcher.getId(), matched.getId());

                return MatchingSimpleResponse.builder()
                    .userId(matched.getId())
                    .age(matched.getAge())
                    .nickname(matched.getNickname())
                    .mainPhoto(matched.getMainPhoto())
                    .matcherTotalScore(matchingInfoDto.getTotalScoreByMatcher())
                    .distance(getDistance(matcher.getId(), matched.getId()))
                    .build();
            }).toList();
        if (responses.isEmpty()) {
            throw new MatchingException(ErrorCode.MATCHING_NOT_FOUND);
        }
        return responses;
    }

    private MatchingInfoDto getCachedMatchingInfo(Long matcherId, Long matchedId) {
        String value = getCachedValue(matcherId, matchedId, DAILY_MATCHING_PREFIX);

        StringTokenizer st = new StringTokenizer(value, "/");
        int totalScore = Integer.parseInt(st.nextToken());
        int mbtiSimilarity = Integer.parseInt(st.nextToken());
        int hobbySimilarity = Integer.parseInt(st.nextToken());
        int lifeStyleSimilarity = Integer.parseInt(st.nextToken());
//        int incomeSimilarity = Integer.parseInt(st.nextToken());

        return MatchingInfoDto.builder()
            .matcherId(matcherId)
            .matchedId(matchedId)
            .totalScoreByMatcher(totalScore)
            .mbtiSimilarity(mbtiSimilarity)
            .hobbySimilarity(hobbySimilarity)
//            .incomeSimilarity(incomeSimilarity)
            .lifeStyleSimilarity(lifeStyleSimilarity)
            .build();
    }

    private void cachMatchingScore(User matcher, User matched,
        MatchingInfoDto matchingInfoDto) {
        String today = getTodayDate();
        String prefix = today + DAILY_MATCHING_PREFIX;
        String key = prefix + matcher.getId() + "_" + matched.getId();
        String value =
            matchingInfoDto.getTotalScoreByMatcher() + "/" + matchingInfoDto.getMbtiSimilarity()
                + "/"
                + matchingInfoDto.getHobbySimilarity() + "/"
                + matchingInfoDto.getLifeStyleSimilarity();
        redisService.setValue(key, value, MATCHING_DURATION);
    }
}

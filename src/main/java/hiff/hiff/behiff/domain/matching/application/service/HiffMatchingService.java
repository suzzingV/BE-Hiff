package hiff.hiff.behiff.domain.matching.application.service;

import static hiff.hiff.behiff.domain.matching.util.Calculator.computeDistance;
import static hiff.hiff.behiff.global.util.DateCalculator.getTodayDate;
import static hiff.hiff.behiff.global.common.batch.hiff_matching.HiffMatchingBatchConfig.matchedQueue;
import static hiff.hiff.behiff.global.util.DateCalculator.getTomorrowDate;

import hiff.hiff.behiff.domain.matching.application.dto.MatchingInfoDto;
import hiff.hiff.behiff.domain.matching.application.dto.NameWithCommonDto;
import hiff.hiff.behiff.domain.matching.application.dto.UserWithMatchCount;
import hiff.hiff.behiff.domain.matching.exception.MatchingException;
import hiff.hiff.behiff.domain.matching.infrastructure.MatchingRepository;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.HiffMatchingDetailResponse;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingSimpleResponse;
import hiff.hiff.behiff.domain.matching.util.SimilarityFactory;
import hiff.hiff.behiff.domain.user.application.UserCRUDService;
import hiff.hiff.behiff.domain.user.application.UserHobbyService;
import hiff.hiff.behiff.domain.user.application.UserLifeStyleService;
import hiff.hiff.behiff.domain.user.application.UserPhotoService;
import hiff.hiff.behiff.domain.user.application.UserPosService;
import hiff.hiff.behiff.domain.user.application.UserWeightValueService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.UserHobby;
import hiff.hiff.behiff.domain.user.domain.entity.UserLifeStyle;
import hiff.hiff.behiff.domain.user.domain.entity.UserPos;
import hiff.hiff.behiff.domain.user.domain.entity.WeightValue;
import hiff.hiff.behiff.domain.user.infrastructure.UserHobbyRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserLifeStyleRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.time.Duration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HiffMatchingService extends MatchingService {

    private final UserCRUDService userCRUDService;
    private final UserWeightValueService userWeightValueService;
    private final UserRepository userRepository;
    private final UserPosService userPosService;
    private final RedisService redisService;
    private final UserHobbyService userHobbyService;
    private final UserHobbyRepository userHobbyRepository;
    private final UserLifeStyleRepository userLifeStyleRepository;
    private final UserLifeStyleService userLifeStyleService;
    private final UserPhotoService userPhotoService;

    private static final Integer HIFF_MATCHING_HEART = 3;
    private static final int TOTAL_SCORE_STANDARD = 80;
    public static final Duration DAILY_HIFF_MATCHING_DURATION = Duration.ofDays(2);
    public static final String HIFF_MATCHING_PREFIX = "hiff_";

    public HiffMatchingService(UserPosService userPosService, RedisService redisService,
        MatchingRepository matchingRepository, SimilarityFactory similarityFactory,
        UserCRUDService userCRUDService, UserWeightValueService userWeightValueService,
        UserRepository userRepository, UserPosService userPosService1, RedisService redisService1,
        MatchingRepository matchingRepository1, SimilarityFactory similarityFactory1,
        UserHobbyService userHobbyService, UserHobbyRepository userHobbyRepository,
        UserLifeStyleRepository userLifeStyleRepository, UserLifeStyleService userLifeStyleService,
        UserPhotoService userPhotoService) {
        super(userPosService, redisService, matchingRepository, similarityFactory);
        this.userCRUDService = userCRUDService;
        this.userWeightValueService = userWeightValueService;
        this.userRepository = userRepository;
        this.userPosService = userPosService1;
        this.redisService = redisService1;
        this.userHobbyService = userHobbyService;
        this.userHobbyRepository = userHobbyRepository;
        this.userLifeStyleRepository = userLifeStyleRepository;
        this.userLifeStyleService = userLifeStyleService;
        this.userPhotoService = userPhotoService;
    }

    // TODO: 남은 시간
    // TODO: 외모 점수 없을 때

    public List<MatchingSimpleResponse> getMatchings(Long userId) {
        String today = getTodayDate();
        List<String> originalMatching = redisService.scanKeysWithPrefix(
            today + HIFF_MATCHING_PREFIX + userId + "_");
        return getCachedMatching(userId, originalMatching);
    }

    public MatchingSimpleResponse performMatching(Long userId) {
        User user = userCRUDService.findById(userId);
        List<User> matcheds = userRepository.getMatched(userId, user.getGender());
        Collections.shuffle(matcheds);
        Queue<User> matchedQueue = new LinkedList<>(matcheds);

        UserPos matcherPos = userPosService.findPosByUserId(userId);
        WeightValue matcherWV = userWeightValueService.findByUserId(userId);
        List<UserHobby> matcherHobbies = userHobbyRepository.findByUserId(userId);
        List<UserLifeStyle> matcherLifeStyles = userLifeStyleRepository.findByUserId(userId);

        while (!matchedQueue.isEmpty()) {
            User matched = matchedQueue.remove();
            if (checkAge(user, matched)) {
                continue;
            }

            UserPos matchedPos = userPosService.findPosByUserId(matched.getId());
            Double distance = computeDistance(matcherPos.getX(), matcherPos.getY(),
                matchedPos.getX(),
                matchedPos.getY());
            if (checkDistance(user, matched, distance)) {
                continue;
            }

            WeightValue matchedWV = userWeightValueService.findByUserId(matched.getId());
            List<UserHobby> matchedHobbies = userHobbyRepository.findByUserId(matched.getId());
            List<UserLifeStyle> matchedLifeStyle = userLifeStyleRepository.findByUserId(
                matched.getId());
            MatchingInfoDto userMatchingInfo = getNewMatchingInfo(user, matched, matcherWV,
                matcherHobbies, matchedHobbies, matcherLifeStyles, matchedLifeStyle);
            MatchingInfoDto matchedMatchingInfo = getNewMatchingInfo(matched, user, matchedWV,
                matchedHobbies, matcherHobbies, matchedLifeStyle, matcherLifeStyles);

            if (checkTotalScore(userMatchingInfo, matchedMatchingInfo)) {
                String today = getTodayDate();
                cachMatchingScore(userId, matched.getId(), userMatchingInfo,
                    matchedMatchingInfo.getTotalScoreByMatcher(), today, MATCHING_DURATION);
                recordMatchingHistory(matched.getId(), userId);
                recordMatchingHistory(userId, matched.getId());
                return MatchingSimpleResponse.builder()
                    .userId(matched.getId())
                    .age(matched.getAge())
                    .nickname(matched.getNickname())
                    .mainPhoto(matched.getMainPhoto())
                    .matcherTotalScore(userMatchingInfo.getTotalScoreByMatcher())
                    .distance(getDistance(user.getId(), matched.getId()))
                    .build();
            }
        }

        throw new MatchingException(ErrorCode.MATCHING_NOT_FOUND);
    }

    public HiffMatchingDetailResponse getMatchingDetails(Long matcherId, Long matchedId) {
        if (!isMatchedBefore(matcherId, matchedId)) {
            throw new MatchingException(ErrorCode.MATCHING_HISTORY_NOT_FOUND);
        }
        User matcher = userCRUDService.findById(matcherId);
        User matched = userCRUDService.findById(matchedId);

        String mainPhoto = matched.getMainPhoto();
        List<String> photos = userPhotoService.getPhotosOfUser(matchedId);
        List<NameWithCommonDto> hobbies = userHobbyService.getHobbiesWithCommon(matcherId, matchedId);
        List<NameWithCommonDto> lifeStyles = userLifeStyleService.getLifeStylesWithCommon(matcherId, matchedId);

        Double distance = getDistance(matcherId, matchedId);
        MatchingInfoDto matchingInfoDto = getCachedMatchingInfo(matcherId, matchedId);

        return HiffMatchingDetailResponse.of(matcher, matched, distance, mainPhoto, photos,
            matchingInfoDto, hobbies, lifeStyles);
    }

    public void dailyMatching(User matcher, PriorityQueue<UserWithMatchCount> matchedArr) {
        UserPos matcherPos = userPosService.findPosByUserId(matcher.getId());
        WeightValue matcherWV = userWeightValueService.findByUserId(matcher.getId());
        List<UserHobby> matcherHobbies = userHobbyRepository.findByUserId(matcher.getId());
        List<UserLifeStyle> matcherLifeStyles = userLifeStyleRepository.findByUserId(
            matcher.getId());
        PriorityQueue<UserWithMatchCount> tmp = new PriorityQueue<>();

        while (!matchedArr.isEmpty()) {
            UserWithMatchCount matchedWithCount = matchedArr.remove();
            User matched = matchedWithCount.getUser();

            if (checkAge(matcher, matched)) {
                tmp.add(matchedWithCount);
                continue;
            }

            if (isMatchedBefore(matcher.getId(), matched.getId())) {
                tmp.add(matchedWithCount);
                continue;
            }

            UserPos matchedPos = userPosService.findPosByUserId(matched.getId());
            Double distance = computeDistance(matcherPos.getX(), matcherPos.getY(),
                matchedPos.getX(),
                matchedPos.getY());
            if (checkDistance(matcher, matched, distance)) {
                tmp.add(matchedWithCount);
                continue;
            }

            WeightValue matchedWV = userWeightValueService.findByUserId(matched.getId());
            List<UserHobby> matchedHobbies = userHobbyRepository.findByUserId(matched.getId());
            List<UserLifeStyle> matchedLifeStyle = userLifeStyleRepository.findByUserId(
                matched.getId());
            MatchingInfoDto matcherMatchingInfo = getNewMatchingInfo(matcher, matched, matcherWV,
                matcherHobbies, matchedHobbies, matcherLifeStyles, matchedLifeStyle);
            MatchingInfoDto matchedMatchingInfo = getNewMatchingInfo(matched, matcher, matchedWV,
                matchedHobbies, matcherHobbies, matchedLifeStyle, matcherLifeStyles);

            if (checkTotalScore(matcherMatchingInfo, matchedMatchingInfo)) {
                matchedWithCount.increaseCount();
                tmp.add(matchedWithCount);

                String tomorrow = getTomorrowDate();
                cachMatchingScore(matcher.getId(), matched.getId(), matcherMatchingInfo,
                    matchedMatchingInfo.getTotalScoreByMatcher(), tomorrow, DAILY_HIFF_MATCHING_DURATION);
                recordMatchingHistory(matched.getId(), matcher.getId());
                recordMatchingHistory(matcher.getId(), matched.getId());
                break;
            }
            tmp.add(matchedWithCount);
        }
        matchedArr.addAll(tmp);
        matchedQueue = matchedArr;
    }

    private MatchingInfoDto getCachedMatchingInfo(Long matcherId, Long matchedId) {
        String today = getTodayDate();
        String value = getCachedValue(matcherId, matchedId, today + HIFF_MATCHING_PREFIX);

        StringTokenizer st = new StringTokenizer(value, "/");
        int totalScoreByMatcher = Integer.parseInt(st.nextToken());
        int totalScoreByMatched = Integer.parseInt(st.nextToken());
        int mbtiSimilarity = Integer.parseInt(st.nextToken());
        int hobbySimilarity = Integer.parseInt(st.nextToken());
        int lifeStyleSimilarity = Integer.parseInt(st.nextToken());
//        int incomeSimilarity = Integer.parseInt(st.nextToken());

        return MatchingInfoDto.builder()
            .matcherId(matcherId)
            .matchedId(matchedId)
            .totalScoreByMatcher(totalScoreByMatcher)
            .totalScoreByMatched(totalScoreByMatched)
            .mbtiSimilarity(mbtiSimilarity)
            .hobbySimilarity(hobbySimilarity)
//            .incomeSimilarity(incomeSimilarity)
            .lifeStyleSimilarity(lifeStyleSimilarity)
            .build();
    }

    private void cachMatchingScore(Long matcherId, Long matchedId, MatchingInfoDto matchingInfoDto,
        int matchedTotalScore, String date, Duration duration) {
        String prefix = date + HIFF_MATCHING_PREFIX;
        String key = prefix + matcherId + "_" + matchedId;
        String value = matchingInfoDto.getTotalScoreByMatcher() + "/" + matchedTotalScore + "/"
            + matchingInfoDto.getMbtiSimilarity() + "/"
            + matchingInfoDto.getHobbySimilarity() + "/"
            + matchingInfoDto.getLifeStyleSimilarity();
        redisService.setValue(key, value, duration);

        key = prefix + matchedId + "_" + matcherId;
        value = matchedTotalScore + "/" + matchingInfoDto.getTotalScoreByMatched() + "/" +
            + matchingInfoDto.getMbtiSimilarity() + "/"
            + matchingInfoDto.getHobbySimilarity() + "/"
            + matchingInfoDto.getLifeStyleSimilarity();
        redisService.setValue(key, value, duration);
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

    private boolean checkTotalScore(MatchingInfoDto matcherMatchingInfo,
        MatchingInfoDto matchedMatchingInfo) {
        return matcherMatchingInfo.getTotalScoreByMatcher() >= TOTAL_SCORE_STANDARD
            && matchedMatchingInfo.getTotalScoreByMatcher() >= TOTAL_SCORE_STANDARD;
    }

    private boolean checkDistance(User matcher, User matched, Double distance) {
        return distance > matcher.getMaxDistance() || distance < matcher.getMinDistance()
            || distance > matched.getMaxDistance() || distance < matched.getMinDistance();
    }

    private boolean checkAge(User matcher, User matched) {
        int matcherAge = matcher.getAge();
        int matchedAge = matched.getAge();
        return matched.getHopeMinAge() > matcherAge || matched.getHopeMaxAge() < matcherAge
            || matcher.getHopeMinAge() > matchedAge
            || matcher.getHopeMaxAge() < matchedAge;
    }
}

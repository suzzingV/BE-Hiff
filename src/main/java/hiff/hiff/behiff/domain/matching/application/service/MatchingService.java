package hiff.hiff.behiff.domain.matching.application.service;

import static hiff.hiff.behiff.domain.matching.util.Calculator.computeDistance;
import static hiff.hiff.behiff.domain.matching.util.Calculator.computeTotalScoreByMatcher;
import static hiff.hiff.behiff.global.common.batch.hiff_matching.HiffMatchingBatchConfig.matchedQueue;
import static hiff.hiff.behiff.global.common.redis.RedisService.HIFF_MATCHING_PREFIX;
import static hiff.hiff.behiff.global.common.redis.RedisService.MATCHING_DURATION;
import static hiff.hiff.behiff.global.common.redis.RedisService.DAILY_MATCHING_PREFIX;
import static hiff.hiff.behiff.global.common.redis.RedisService.NOT_EXIST;
import static hiff.hiff.behiff.global.common.redis.RedisService.PAID_DAILY_MATCHING_PREFIX;

import hiff.hiff.behiff.domain.matching.application.dto.MatchingInfoDto;
import hiff.hiff.behiff.domain.matching.application.dto.NameWithCommonDto;
import hiff.hiff.behiff.domain.matching.domain.entity.Matching;
import hiff.hiff.behiff.domain.matching.exception.MatchingException;
import hiff.hiff.behiff.domain.matching.infrastructure.MatchingRepository;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingDetailResponse;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchingService {

    private final UserCRUDService userCRUDService;
    private final UserWeightValueService userWeightValueService;
    private final UserRepository userRepository;
    private final UserPosService userPosService;
    private final RedisService redisService;
    private final MatchingRepository matchingRepository;
    private final SimilarityFactory similarityFactory;
    private final UserHobbyService userHobbyService;
    private final UserHobbyRepository userHobbyRepository;
    private final UserLifeStyleRepository userLifeStyleRepository;
    private final UserLifeStyleService userLifeStyleService;
    private final UserPhotoService userPhotoService;

    private static final Integer DAILY_MATCHING_HEART = 1;
    private static final Integer HIFF_MATCHING_HEART = 3;
    private static final int TOTAL_SCORE_STANDARD = 80;

    // TODO: 남은 시간
    // TODO: 외모 점수 없을 때
    public List<MatchingSimpleResponse> getDailyMatching(Long userId) {
        User matcher = userCRUDService.findById(userId);
        List<String> originalMatching = redisService.scanKeysWithPrefix(
            DAILY_MATCHING_PREFIX + userId + "_");
        if (!originalMatching.isEmpty()) {
            return getCachedMatching(userId, originalMatching);
        }

        return getNewDailyMatching(matcher, DAILY_MATCHING_PREFIX);
    }

    public List<MatchingSimpleResponse> getPaidDailyMatching(Long userId) {
        User matcher = userCRUDService.findById(userId);
        useHeart(matcher, DAILY_MATCHING_HEART);
        List<String> originalMatching = redisService.scanKeysWithPrefix(
            PAID_DAILY_MATCHING_PREFIX + userId + "_");
        List<MatchingSimpleResponse> newMatching = getNewDailyMatching(matcher,
            PAID_DAILY_MATCHING_PREFIX);

        originalMatching.forEach(redisService::delete);
        return newMatching;
    }

    public MatchingDetailResponse getDailyMatchingDetails(Long matcherId, Long matchedId) {
        if(!isMatchedBefore(matcherId, matchedId)) {
            throw new MatchingException(ErrorCode.MATCHING_NOT_FOUND);
        }
        User matcher = userCRUDService.findById(matcherId);
        User matched = userCRUDService.findById(matchedId);

        List<String> photos = userPhotoService.getPhotosOfUser(matchedId);
        List<NameWithCommonDto> hobbies = getHobbiesWithCommon(matcherId, matchedId);
        List<NameWithCommonDto> lifeStyles = getLifeStylesWithCommon(matcherId, matchedId);

        Double distance = getDistance(matcherId, matchedId);
        MatchingInfoDto matchingInfoDto = getCachedMatchingInfo(matcherId, matchedId);

        return MatchingDetailResponse.of(matcher, matched, distance, photos, matchingInfoDto,
            hobbies, lifeStyles);
    }

    public void getNewHiffMatching(User matcher, PriorityQueue<UserWithMatchCount> matchedArr) {
        UserPos matcherPos = userPosService.findPosByUserId(matcher.getId());
        WeightValue matcherWV = userWeightValueService.findByUserId(matcher.getId());
        List<UserHobby> matcherHobbies = userHobbyRepository.findByUserId(matcher.getId());
        List<UserLifeStyle> matcherLifeStyles = userLifeStyleRepository.findByUserId(matcher.getId());
        PriorityQueue<UserWithMatchCount> tmp = new PriorityQueue<>();

        while (!matchedArr.isEmpty()) {
            UserWithMatchCount matchedWithCount = matchedArr.remove();
            User matched = matchedWithCount.getUser();

            if(checkAge(matcher, matched)) {
                tmp.add(matchedWithCount);
                continue;
            }

            if(isMatchedBefore(matcher.getId(), matched.getId())) {
                tmp.add(matchedWithCount);
                continue;
            }

            UserPos matchedPos = userPosService.findPosByUserId(matched.getId());
            Double distance = computeDistance(matcherPos.getX(), matcherPos.getY(), matchedPos.getX(),
                matchedPos.getY());
            if (checkDistance(matcher, matched, distance)) {
                tmp.add(matchedWithCount);
                continue;
            }

            WeightValue matchedWV = userWeightValueService.findByUserId(matched.getId());
            List<UserHobby> matchedHobbies = userHobbyRepository.findByUserId(matched.getId());
            List<UserLifeStyle> matchedLifeStyle = userLifeStyleRepository.findByUserId(matched.getId());
            MatchingInfoDto matcherMatchingInfo = getNewHiffMatchingInfo(matcher, matched, matcherWV, matcherHobbies, matchedHobbies, matcherLifeStyles, matchedLifeStyle);
            MatchingInfoDto matchedMatchingInfo = getNewHiffMatchingInfo(matched, matcher, matchedWV, matchedHobbies, matcherHobbies, matchedLifeStyle, matcherLifeStyles);

            if (checkTotalScore(matcherMatchingInfo, matchedMatchingInfo)) {
                matchedWithCount.increaseCount();
                tmp.add(matchedWithCount);

                String today = getTodayDate();
                String prefix = today + HIFF_MATCHING_PREFIX;
                cachMatchingScore(matched, matcher, matcherMatchingInfo, prefix);
                recordMatchingHistory(matched.getId(), matcher.getId());
                recordMatchingHistory(matcher.getId(), matched.getId());
                break;
            }
            tmp.add(matchedWithCount);
        }
        matchedArr.addAll(tmp);
        matchedQueue = matchedArr;
    }

    private String getTodayDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        return today.format(formatter);
    }

    private boolean checkTotalScore(MatchingInfoDto matcherMatchingInfo,
        MatchingInfoDto matchedMatchingInfo) {
        return matcherMatchingInfo.getTotalScore() >= TOTAL_SCORE_STANDARD
            && matchedMatchingInfo.getTotalScore() >= TOTAL_SCORE_STANDARD;
    }

    private boolean checkDistance(User matcher, User matched, Double distance) {
        return distance > matcher.getMaxDistance() || distance < matcher.getMinDistance()
            || distance > matched.getMaxDistance() || distance < matched.getMinDistance();
    }

    private boolean isMatchedBefore(Long matcherId, Long matchedId) {
        List<Long> matchingHistory = matchingRepository.findByUsers(matcherId, matchedId);
        return !matchingHistory.isEmpty();
    }

    private boolean checkAge(User matcher, User matched) {
        int matcherAge = matcher.getAge();
        int matchedAge = matched.getAge();
        return matched.getHopeMinAge() > matcherAge || matched.getHopeMaxAge() < matcherAge
            || matcher.getHopeMinAge() > matchedAge
            || matcher.getHopeMaxAge() < matchedAge;
    }

    private void useHeart(User matcher, Integer amount) {
        if (matcher.getHeart() < amount) {
            throw new MatchingException(ErrorCode.LACK_OF_HEART);
        }
        matcher.subtractHeart(amount);
    }

    private List<MatchingSimpleResponse> getCachedMatching(Long userId,
        List<String> originalMatching) {
        return originalMatching.stream()
            .map(key -> {
                Long matchedId = getMatchedId(key);
                User matched = userCRUDService.findById(matchedId);
                int totalScore = getTotalScore(key);

                return MatchingSimpleResponse.builder()
                    .userId(matchedId)
                    .age(matched.getAge())
                    .nickname(matched.getNickname())
                    .mainPhoto(matched.getMainPhoto())
                    .totalScore(totalScore)
                    .distance(getDistance(userId, matchedId))
                    .build();
            }).toList();
    }

    private List<MatchingSimpleResponse> getNewDailyMatching(User matcher, String prefix) {
        List<MatchingSimpleResponse> responses = userRepository.getDailyMatched(matcher.getId(),
                matcher.getGender())
            .stream()
            .map(matched -> getAndRecordMatchingInfo(matched, matcher, prefix)).toList();
        if (responses.isEmpty()) {
            throw new MatchingException(ErrorCode.MATCHING_NOT_FOUND);
        }
        return responses;
    }

    private String getCachedMatchingValue(Long matcherId, Long matchedId) {
        String key = DAILY_MATCHING_PREFIX + matcherId + "_" + matchedId;
        String matchingValue = redisService.getStrValue(key);
        if (matchingValue.equals(NOT_EXIST)) {
            throw new MatchingException(ErrorCode.MATCHING_SCORE_NOT_FOUND);
        }
        return matchingValue;
    }

    private MatchingInfoDto getCachedMatchingInfo(Long matcherId, Long matchedId) {
        String matchingValue = getCachedMatchingValue(matcherId, matchedId);

        StringTokenizer st = new StringTokenizer(matchingValue, "/");
        int totalScore = Integer.parseInt(st.nextToken());
        int mbtiSimilarity = Integer.parseInt(st.nextToken());
        int hobbySimilarity = Integer.parseInt(st.nextToken());
        int lifeStyleSimilarity = Integer.parseInt(st.nextToken());
//        int incomeSimilarity = Integer.parseInt(st.nextToken());

        return MatchingInfoDto.builder()
            .totalScore(totalScore)
            .mbtiSimilarity(mbtiSimilarity)
            .hobbySimilarity(hobbySimilarity)
//            .incomeSimilarity(incomeSimilarity)
            .lifeStyleSimilarity(lifeStyleSimilarity)
            .build();
    }

    private List<NameWithCommonDto> getLifeStylesWithCommon(Long matcherId, Long matchedId) {
        List<String> matcherLifeStyles = userLifeStyleService.findLifeStylesByUser(matcherId);
        List<String> matchedLifeStyles = userLifeStyleService.findLifeStylesByUser(matchedId);

        return matchedLifeStyles.stream()
            .map(lifeStyle -> {
                boolean isCommon = matcherLifeStyles.contains(lifeStyle);
                return NameWithCommonDto.builder()
                    .name(lifeStyle)
                    .isCommon(isCommon)
                    .build();
            }).toList();
    }

    private List<NameWithCommonDto> getHobbiesWithCommon(Long matcherId, Long matchedId) {
        List<String> matcherHobbies = userHobbyService.findHobbiesByUser(matcherId);
        List<String> matchedHobbies = userHobbyService.findHobbiesByUser(matchedId);

        return matchedHobbies.stream()
            .map(hobby -> {
                boolean isCommon = matcherHobbies.contains(hobby);
                return NameWithCommonDto.builder()
                    .name(hobby)
                    .isCommon(isCommon)
                    .build();
            }).toList();
    }

    private MatchingSimpleResponse getAndRecordMatchingInfo(User matched, User matcher,
        String prefix) {
        WeightValue matcherWV = userWeightValueService.findByUserId(matcher.getId());
        MatchingInfoDto matchingInfoDto = getNewMatchingInfo(matcher, matched, matcherWV);

        cachMatchingScore(matcher, matched, matchingInfoDto, prefix);
        recordMatchingHistory(matcher.getId(), matched.getId());

        return MatchingSimpleResponse.builder()
            .userId(matched.getId())
            .age(matched.getAge())
            .nickname(matched.getNickname())
            .mainPhoto(matched.getMainPhoto())
            .totalScore(matchingInfoDto.getTotalScore())
            .distance(getDistance(matcher.getId(), matched.getId()))
            .build();
    }

    private MatchingInfoDto getNewHiffMatchingInfo(User matcher, User matched,
        WeightValue matcherWV, List<UserHobby> matcherHobbies, List<UserHobby> matchedHobbies, List<UserLifeStyle> matcherLifeStyles, List<UserLifeStyle> matchedLifeStyles) {
        int mbtiSimilarity = similarityFactory.getMbtiSimilarity(matcher, matched);
        int hobbySimilarity = similarityFactory.getHobbySimilarity(matcherHobbies, matchedHobbies);
        int lifeStyleSimilarity = similarityFactory.getLifeStyleSimilarity(matcherLifeStyles, matchedLifeStyles);
//        int incomeSimilarity = similarityFactory.getIncomeSimilarity(matcher, matched);
        Integer totalScore = computeTotalScoreByMatcher(matcherWV, mbtiSimilarity, hobbySimilarity,
            lifeStyleSimilarity, matched.getEvaluatedScore());

        return MatchingInfoDto.builder()
            .mbtiSimilarity(mbtiSimilarity)
            .hobbySimilarity(hobbySimilarity)
            .lifeStyleSimilarity(lifeStyleSimilarity)
//            .incomeSimilarity(incomeSimilarity)
            .totalScore(totalScore)
            .build();
    }


    private MatchingInfoDto getNewMatchingInfo(User matcher, User matched,
        WeightValue matcherWV) {
        List<UserHobby> matcherHobbies = userHobbyRepository.findByUserId(matcher.getId());
        List<UserHobby> matchedHobbies = userHobbyRepository.findByUserId(matched.getId());
        List<UserLifeStyle> matcherLifeStyles = userLifeStyleRepository.findByUserId(
            matcher.getId());
        List<UserLifeStyle> matchedLifeStyles = userLifeStyleRepository.findByUserId(
            matched.getId());
        int mbtiSimilarity = similarityFactory.getMbtiSimilarity(matcher, matched);
        int hobbySimilarity = similarityFactory.getHobbySimilarity(matcherHobbies, matchedHobbies);
        int lifeStyleSimilarity = similarityFactory.getLifeStyleSimilarity(matcherLifeStyles, matchedLifeStyles);
//        int incomeSimilarity = similarityFactory.getIncomeSimilarity(matcher, matched);
        Integer totalScore = computeTotalScoreByMatcher(matcherWV, mbtiSimilarity, hobbySimilarity,
            lifeStyleSimilarity, matched.getEvaluatedScore());

        return MatchingInfoDto.builder()
            .mbtiSimilarity(mbtiSimilarity)
            .hobbySimilarity(hobbySimilarity)
            .lifeStyleSimilarity(lifeStyleSimilarity)
//            .incomeSimilarity(incomeSimilarity)
            .totalScore(totalScore)
            .build();
    }

    private void recordMatchingHistory(Long userId, Long matchedId) {
        Matching matching = Matching.builder()
            .matchedId(matchedId)
            .matcherId(userId)
            .build();
        matchingRepository.save(matching);
    }

    private void cachMatchingScore(User matcher, User matched, MatchingInfoDto matchingInfoDto,
        String prefix) {
        String key = prefix + matcher.getId() + "_" + matched.getId();
        String value = matchingInfoDto.getTotalScore() + "/" + matchingInfoDto.getMbtiSimilarity() + "/"
            + matchingInfoDto.getHobbySimilarity() + "/"
            + matchingInfoDto.getLifeStyleSimilarity();
        redisService.setStrValue(key, value, MATCHING_DURATION);
    }

    private int getTotalScore(String key) {
        StringTokenizer st = new StringTokenizer(redisService.getStrValue(key), "/");
        return Integer.parseInt(st.nextToken());
    }

    private Long getMatchedId(String key) {
        StringTokenizer st = new StringTokenizer(key, "_");
        st.nextToken();
        st.nextToken();
        return Long.parseLong(st.nextToken());
    }

    private Double getDistance(Long matcherId, Long matchedId) {
        UserPos matcherPos = userPosService.findPosByUserId(matcherId);
        UserPos matchedPos = userPosService.findPosByUserId(matchedId);

        return computeDistance(matcherPos.getX(), matcherPos.getY(), matchedPos.getX(), matchedPos.getY());
    }
}

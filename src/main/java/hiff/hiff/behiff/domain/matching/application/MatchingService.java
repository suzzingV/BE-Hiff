package hiff.hiff.behiff.domain.matching.application;

import static hiff.hiff.behiff.domain.matching.util.Calculator.computeDistance;
import static hiff.hiff.behiff.domain.matching.util.Calculator.computeTotalScoreByMatcher;
import static hiff.hiff.behiff.global.common.redis.RedisService.MATCHING_DURATION;
import static hiff.hiff.behiff.global.common.redis.RedisService.MATCHING_PREFIX;
import static hiff.hiff.behiff.global.common.redis.RedisService.NOT_EXIST;

import hiff.hiff.behiff.domain.matching.domain.entity.Matching;
import hiff.hiff.behiff.domain.matching.exception.MatchingException;
import hiff.hiff.behiff.domain.matching.infrastructure.MatchingRepository;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingDetailResponse;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingSimpleResponse;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.NameWithCommonDto;
import hiff.hiff.behiff.domain.matching.util.SimilarityFactory;
import hiff.hiff.behiff.domain.user.application.UserCRUDService;
import hiff.hiff.behiff.domain.user.application.UserHobbyService;
import hiff.hiff.behiff.domain.user.application.UserLifeStyleService;
import hiff.hiff.behiff.domain.user.application.UserPhotoService;
import hiff.hiff.behiff.domain.user.application.UserPosService;
import hiff.hiff.behiff.domain.user.application.UserWeightValueService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.UserPos;
import hiff.hiff.behiff.domain.user.domain.entity.WeightValue;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.util.List;
import java.util.StringTokenizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MatchingService {

    private final UserCRUDService userCRUDService;
    private final UserWeightValueService userWeightValueService;
    private final UserRepository userRepository;
    private final UserPosService userPosService;
    private final RedisService redisService;
    private final MatchingRepository matchingRepository;
    private final SimilarityFactory similarityFactory;
    private final UserHobbyService userHobbyService;
    private final UserLifeStyleService userLifeStyleService;
    private final UserPhotoService userPhotoService;

    public List<MatchingSimpleResponse> getDailyMatching(Long userId) {
        //TODO: 거리계산 논의 필요
        User matcher = userCRUDService.findUserById(userId);
        List<String> matchingScores = redisService.scanKeysWithPrefix(
            MATCHING_PREFIX + userId + "_");

        if (redisService.isExistInt(MATCHING_PREFIX + userId)) {
            return matchingScores.stream()
                .map(key -> getCachedMatching(userId, key)).toList();
        }

        //TODO: 더이상 새로 매칭할 사람이 없거나 5명 이하면 어떡해?
        List<MatchingSimpleResponse> responses = userRepository.getFiveMatched(userId,
                matcher.getGender())
            .stream()
            .map(matched -> getNewMatching(matched, matcher)).toList();

        redisService.setIntValue(MATCHING_PREFIX + userId, 0, MATCHING_DURATION);
        matchingScores.forEach(redisService::delete);
        return responses;
    }

    public List<MatchingSimpleResponse> getPaidDailyMatching(Long userId) {
        //TODO: 유료결제 했는데 딱 쿨타임 돌면 어떡해?
        User matcher = userCRUDService.findUserById(userId);
        matcher.subtractHeart(1);
        List<String> matchingScores = redisService.scanKeysWithPrefix(
            MATCHING_PREFIX + userId + "_");
        List<MatchingSimpleResponse> responses = userRepository.getFiveMatched(userId,
                matcher.getGender())
            .stream()
            .map(matched -> getNewMatching(matched, matcher)).toList();

        matchingScores.forEach(redisService::delete);
        return responses;
    }

    public MatchingDetailResponse getDailyMatchingDetails(Long matcherId, Long matchedId) {
        checkMatching(matcherId, matchedId);
        User matcher = userCRUDService.findUserById(matcherId);
        User matched = userCRUDService.findUserById(matchedId);

        List<String> photos = userPhotoService.getPhotosOfUser(matchedId);
        List<NameWithCommonDto> hobbies = getHobbiesWithCommon(matcherId, matchedId);
        List<NameWithCommonDto> lifeStyles = getLifeStylesWithCommon(matcherId, matchedId);

        String key = MATCHING_PREFIX + matcherId + "_" + matchedId;
        String matchingScore = getMatchingScore(key);

        StringTokenizer st = new StringTokenizer(matchingScore, "/");
        int totalScore = Integer.parseInt(st.nextToken());
        int mbtiSimilarity = Integer.parseInt(st.nextToken());
        int hobbySimilarity = Integer.parseInt(st.nextToken());
        int lifeStyleSimilarity = Integer.parseInt(st.nextToken());
        int incomeSimilarity = Integer.parseInt(st.nextToken());

        return MatchingDetailResponse.builder()
            .matchedId(matchedId)
            .nickname(matched.getNickname())
            .age(matched.getAge())
            .distance(getDistance(matcherId, matchedId))
            .photos(photos)
            .totalScore(totalScore)
            .myMbti(matcher.getMbti())
            .matchedMbti(matched.getMbti())
            .mbtiSimilarity(mbtiSimilarity)
            .myIncome(matcher.getIncome())
            .matchedIncome(matched.getIncome())
            .incomeSimilarity(incomeSimilarity)
            .hobbies(hobbies)
            .hobbySimilarity(hobbySimilarity)
            .lifeStyles(lifeStyles)
            .lifeStyleSimilarity(lifeStyleSimilarity)
            .build();
    }

    private String getMatchingScore(String key) {
        String matchingScore = redisService.getStrValue(key);
        if (matchingScore.equals(NOT_EXIST)) {
            throw new MatchingException(ErrorCode.MATCHING_SCORE_NOT_FOUND);
        }
        return matchingScore;
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

    private void checkMatching(Long matcherId, Long matchedId) {
        matchingRepository.findByMatcherIdAndMatchedId(matcherId, matchedId)
            .orElseThrow(() -> new MatchingException(ErrorCode.MATCHING_NOT_FOUND));
    }

    private MatchingSimpleResponse getNewMatching(User matched, User matcher) {
        WeightValue matcherWV = userWeightValueService.findByUserId(matcher.getId());
        Integer totalScore = calculateTotalScoreAndCach(matcher, matched, matcherWV);

        recordMatchingHistory(matcher.getId(), matched);

        return MatchingSimpleResponse.builder()
            .userId(matched.getId())
            .age(matched.getAge())
            .nickname(matched.getNickname())
            .mainPhoto(matched.getMainPhoto())
            .totalScore(totalScore)
            .distance(getDistance(matcher.getId(), matched.getId()))
            .build();
    }

    private MatchingSimpleResponse getCachedMatching(Long userId, String key) {
        Long matchedId = getMatchedId(key);
        User matched = userCRUDService.findUserById(matchedId);
        int totalScore = getTotalScore(key);

        return MatchingSimpleResponse.builder()
            .userId(matchedId)
            .age(matched.getAge())
            .nickname(matched.getNickname())
            .mainPhoto(matched.getMainPhoto())
            .totalScore(totalScore)
            .distance(getDistance(userId, matchedId))
            .build();
    }

    private void recordMatchingHistory(Long userId, User matched) {
        Matching matching = Matching.builder()
            .matchedId(matched.getId())
            .matcherId(userId)
            .build();
        matchingRepository.save(matching);
    }

    private int calculateTotalScoreAndCach(User matcher, User matched, WeightValue matcherWV) {
        int mbtiSimilarity = similarityFactory.getMbtiSimilarity(matcher, matched);
        int hobbySimilarity = similarityFactory.getHobbySimilarity(matcher, matched);
        int lifeStyleSimilarity = similarityFactory.getLifeStyleSimilarity(matcher, matched);
        int incomeSimilarity = similarityFactory.getIncomeSimilarity(matcher, matched);
        Integer totalScore = computeTotalScoreByMatcher(matcherWV, mbtiSimilarity, hobbySimilarity,
            lifeStyleSimilarity, incomeSimilarity);

        String key = MATCHING_PREFIX + matcher.getId() + "_" + matched.getId();
        String value =
            totalScore + "/" + mbtiSimilarity + "/" + hobbySimilarity + "/" + lifeStyleSimilarity
                + "/" + incomeSimilarity;
        redisService.setStrValue(key, value, MATCHING_DURATION);
        return totalScore;
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

        double matcherX = Double.parseDouble(matcherPos.getX());
        double matcherY = Double.parseDouble(matcherPos.getY());
        double matchedX = Double.parseDouble(matchedPos.getX());
        double matchedY = Double.parseDouble(matchedPos.getY());

        return computeDistance(matcherX, matcherY, matchedX, matchedY);
    }
}

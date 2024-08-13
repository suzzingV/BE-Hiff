package hiff.hiff.behiff.domain.matching.application;

import static hiff.hiff.behiff.domain.matching.util.Calculator.computeDistance;
import static hiff.hiff.behiff.domain.matching.util.Calculator.computeTotalScoreByMatcher;
import static hiff.hiff.behiff.global.common.redis.RedisService.MATCHING_DURATION;
import static hiff.hiff.behiff.global.common.redis.RedisService.MATCHING_PREFIX;

import hiff.hiff.behiff.domain.matching.domain.entity.Matching;
import hiff.hiff.behiff.domain.matching.infrastructure.MatchingRepository;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingSimpleResponse;
import hiff.hiff.behiff.domain.matching.util.SimilarityFactory;
import hiff.hiff.behiff.domain.user.application.UserCRUDService;
import hiff.hiff.behiff.domain.user.application.UserPosService;
import hiff.hiff.behiff.domain.user.application.UserWeightValueService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.UserPos;
import hiff.hiff.behiff.domain.user.domain.entity.WeightValue;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.global.common.redis.RedisService;
import java.util.List;
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

    public List<MatchingSimpleResponse> getDailyMatching(Long userId) {
        User matcher = userCRUDService.findUserById(userId);
        List<String> matchings = redisService.scanKeysWithPrefix(MATCHING_PREFIX + userId + "_");

        if (redisService.isExistInt(MATCHING_PREFIX + userId)) {
            return matchings.stream()
                .map(key -> getCachedMatching(userId, key)).toList();
        }

        redisService.setIntValue(MATCHING_PREFIX + userId, 0, MATCHING_DURATION);
        matchings.forEach(redisService::delete);

        return userRepository.getFiveMatched(userId, matcher.getGender())
            .stream()
            .map(matched -> getNewMatching(userId, matched, matcher)).toList();
    }

    private MatchingSimpleResponse getNewMatching(Long userId, User matched,
        User matcher) {
        WeightValue matcherWV = userWeightValueService.findByUserId(matcher.getId());
        Integer totalScore = calculateTotalScoreAndCach(matcher, matched, matcherWV);

        recordMatchingHistory(userId, matched);

        return MatchingSimpleResponse.builder()
            .userId(matched.getId())
            .age(matched.getAge())
            .nickname(matched.getNickname())
            .mainPhoto(matched.getMainPhoto())
            .totalScore(totalScore)
            .distance(getDistance(userId, matched.getId()))
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

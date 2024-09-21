package hiff.hiff.behiff.domain.matching.application.service;

import static hiff.hiff.behiff.domain.matching.util.Calculator.computeDistance;
import static hiff.hiff.behiff.domain.matching.util.Calculator.computeTotalScoreByMatcher;
import static hiff.hiff.behiff.global.common.redis.RedisService.NOT_EXIST;

import hiff.hiff.behiff.domain.matching.application.dto.MatchingInfoDto;
import hiff.hiff.behiff.domain.matching.domain.entity.Matching;
import hiff.hiff.behiff.domain.matching.exception.MatchingException;
import hiff.hiff.behiff.domain.matching.infrastructure.MatchingRepository;
import hiff.hiff.behiff.domain.matching.util.SimilarityFactory;
import hiff.hiff.behiff.domain.user.application.UserPosService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.UserHobby;
import hiff.hiff.behiff.domain.user.domain.entity.UserLifeStyle;
import hiff.hiff.behiff.domain.user.domain.entity.UserPos;
import hiff.hiff.behiff.domain.user.domain.entity.WeightValue;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.time.Duration;
import java.util.List;
import java.util.StringTokenizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchingService {

    private final UserPosService userPosService;
    private final RedisService redisService;
    private final MatchingRepository matchingRepository;
    private final SimilarityFactory similarityFactory;

    public static final Duration MATCHING_DURATION = Duration.ofDays(1);


    protected String getCachedValue(Long matcherId, Long matchedId, String prefix) {
        String key = prefix + matcherId + "_" + matchedId;
        String value = redisService.getStrValue(key);
        if (value.equals(NOT_EXIST)) {
            throw new MatchingException(ErrorCode.MATCHING_SCORE_NOT_FOUND);
        }
        return value;
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

    protected void recordMatchingHistory(Long userId, Long matchedId) {
        Matching matching = Matching.builder()
            .matchedId(matchedId)
            .matcherId(userId)
            .build();
        matchingRepository.save(matching);
    }

    protected Double getDistance(Long matcherId, Long matchedId) {
        UserPos matcherPos = userPosService.findPosByUserId(matcherId);
        UserPos matchedPos = userPosService.findPosByUserId(matchedId);

        return computeDistance(matcherPos.getLat(), matcherPos.getLon(), matchedPos.getLat(),
            matchedPos.getLon());
    }

    protected void useHeart(User matcher, Integer amount) {
        if (matcher.getHeart() < amount) {
            throw new MatchingException(ErrorCode.LACK_OF_HEART);
        }
        matcher.subtractHeart(amount);
    }

    protected boolean isMatchedBefore(Long matcherId, Long matchedId) {
        List<Long> matchingHistory = matchingRepository.findByUsers(matcherId, matchedId);
        return !matchingHistory.isEmpty();
    }

    protected Long getMatchedIdFromKey(String key) {
        StringTokenizer st = new StringTokenizer(key, "_");
        st.nextToken();
        st.nextToken();
        return Long.parseLong(st.nextToken());
    }


}

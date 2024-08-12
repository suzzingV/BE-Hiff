package hiff.hiff.behiff.domain.matching.application;

import static hiff.hiff.behiff.domain.matching.util.Calculator.*;
import static hiff.hiff.behiff.global.common.redis.RedisService.HOBBY_PREFIX;
import static hiff.hiff.behiff.global.common.redis.RedisService.INCOME_PREFIX;
import static hiff.hiff.behiff.global.common.redis.RedisService.LIFESTYLE_PREFIX;
import static hiff.hiff.behiff.global.common.redis.RedisService.MBTI_PREFIX;

import hiff.hiff.behiff.domain.matching.exception.MatchingException;
import hiff.hiff.behiff.domain.matching.presentation.dto.req.MatchingRequest;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingSimpleResponse;
import hiff.hiff.behiff.domain.matching.util.Calculator;
import hiff.hiff.behiff.domain.user.application.UserCRUDService;
import hiff.hiff.behiff.domain.user.application.UserWeightValueService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.UserHobby;
import hiff.hiff.behiff.domain.user.domain.entity.UserLifeStyle;
import hiff.hiff.behiff.domain.user.domain.entity.UserPos;
import hiff.hiff.behiff.domain.user.domain.entity.WeightValue;
import hiff.hiff.behiff.domain.user.infrastructure.UserHobbyRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserLifeStyleRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserPosRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.util.List;
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
    private final UserPosRepository userPosRepository;
    private final RedisService redisService;
    private final UserHobbyRepository userHobbyRepository;
    private final UserLifeStyleRepository userLifeStyleRepository;

    public List<MatchingSimpleResponse> getGeneralMatching(Long userId, MatchingRequest request) {
        User matcher = userCRUDService.findUserById(userId);
        if(redis)
        return userRepository.getFiveMatched(userId, matcher.getGender())
            .stream()
            .map(matched -> {
                String nickname = matched.getNickname();
                Integer age = matched.getAge();
                Integer totalScore = getTotalScoreByMatcher(matcher, matched);
                String mainPhoto = matcher.getMainPhoto();
                Double distance = getDistance(userId, matched.getId());

                return MatchingSimpleResponse.builder()
                    .age(age)
                    .nickname(nickname)
                    .mainPhoto(mainPhoto)
                    .totalScore(totalScore)
                    .distance(distance)
                    .build();
            }).toList();
    }

    private Double getDistance(Long matcherId, Long matchedId) {
        UserPos matcherPos = findPosByUserId(matcherId);
        UserPos matchedPos = findPosByUserId(matchedId);

        double matcherX = Double.parseDouble(matcherPos.getX());
        double matcherY = Double.parseDouble(matcherPos.getY());
        double matchedX = Double.parseDouble(matchedPos.getX());
        double matchedY = Double.parseDouble(matchedPos.getY());

        return computeDistance(matcherX, matcherY, matchedX, matchedY);
    }

    private UserPos findPosByUserId(Long userId) {
        return userPosRepository.findByUserId(userId)
            .orElseThrow(() -> new MatchingException(ErrorCode.USER_POS_NOT_FOUND));
    }

    private Integer getTotalScoreByMatcher(User matcher, User matched) {
        WeightValue matcherWV = userWeightValueService.findByUserId(matcher.getId());
        Long matcherId = matcher.getId();
        Long matchedId = matched.getId();

        int mbtiSimilarity = getMbtiSimilarity(matcher, matched);
        int hobbySimilarity = getHobbySimilarity(matcherId, matchedId);
        int lifeStyleSimilarity = getLifeStyleSimilarity(matcherId, matchedId);
        int incomeSimilarity = getIncomeSimilarity(matcher, matched);
        return computeTotalScoreByMatcher(matcherWV, mbtiSimilarity, hobbySimilarity, lifeStyleSimilarity, incomeSimilarity);
    }

    private int getIncomeSimilarity(User matcher, User matched) {
        return redisService.getIntValue(
            INCOME_PREFIX + matcher.getIncome().getStartValue() + "_" + matched.getIncome()
                .getStartValue());
    }

    private int getLifeStyleSimilarity(Long matcherId, Long matchedId) {
        List<UserLifeStyle> matcherLifeStyles = userLifeStyleRepository.findByUserId(matcherId);
        List<UserLifeStyle> matchedLifeStyles = userLifeStyleRepository.findByUserId(matchedId);

        int lifeStyleSimilaritySum = 0;
        for(UserLifeStyle matcherLifeStyle : matcherLifeStyles) {
            for(UserLifeStyle matchedLifeStyle : matchedLifeStyles) {
                lifeStyleSimilaritySum += redisService.getIntValue(
                    LIFESTYLE_PREFIX + matcherLifeStyle.getLifeStyleId() + "_" + matchedLifeStyle.getLifeStyleId());
            }
        }
        return computeIntAvg(lifeStyleSimilaritySum, matcherLifeStyles.size() * matchedLifeStyles.size());
    }

    private int getHobbySimilarity(Long matcherId, Long matchedId) {
        List<UserHobby> matcherHobbies = userHobbyRepository.findByUserId(matcherId);
        List<UserHobby> matchedHobbies = userHobbyRepository.findByUserId(matchedId);

        int hobbySimilaritySum = 0;
        for(UserHobby matcherHobby : matcherHobbies) {
            for(UserHobby matchedHobby : matchedHobbies) {
                hobbySimilaritySum += redisService.getIntValue(
                    HOBBY_PREFIX + matcherHobby.getHobbyId() + "_" + matchedHobby.getHobbyId());
            }
        }

        return computeIntAvg(hobbySimilaritySum, matcherHobbies.size() * matchedHobbies.size());
    }

    private int getMbtiSimilarity(User matcher, User matched) {
        return redisService.getIntValue(
            MBTI_PREFIX + matcher.getMbti() + "_" + matched.getMbti());
    }
}

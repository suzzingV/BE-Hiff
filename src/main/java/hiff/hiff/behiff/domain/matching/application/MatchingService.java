package hiff.hiff.behiff.domain.matching.application;

import static hiff.hiff.behiff.domain.matching.util.Calculator.*;
import static hiff.hiff.behiff.global.common.redis.RedisService.*;

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
import java.util.Map;
import java.util.StringTokenizer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public List<MatchingSimpleResponse> getGeneralMatching(Long userId) {
        User matcher = userCRUDService.findUserById(userId);
        List<String> matchings = redisService.scanKeysWithPrefix(MATCHING_PREFIX + userId + "_");
        if(redisService.isExistInt(MATCHING_PREFIX + userId)) {
            return matchings.stream()
                    .map(key -> {
                        StringTokenizer st = new StringTokenizer(key, "_");
                        st.nextToken();
                        st.nextToken();
                        Long matchedId = Long.parseLong(st.nextToken());
                        User matched = userCRUDService.findUserById(matchedId);
                        st = new StringTokenizer(redisService.getStrValue(key), "/");
                        int totalScore = Integer.parseInt(st.nextToken());

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

        redisService.setIntValue(MATCHING_PREFIX + userId, 0, MATCHING_DURATION);
        matchings.forEach(redisService::delete);

        return userRepository.getFiveMatched(userId, matcher.getGender())
            .stream()
            .map(matched -> {
                String nickname = matched.getNickname();
                Integer age = matched.getAge();
                int mbtiSimilarity = getMbtiSimilarity(matcher, matched);
                int hobbySimilarity = getHobbySimilarity(matcher.getId(), matched.getId());
                int lifeStyleSimilarity = getLifeStyleSimilarity(matcher.getId(), matched.getId());
                int incomeSimilarity = getIncomeSimilarity(matcher, matched);
                WeightValue matcherWV = userWeightValueService.findByUserId(matcher.getId());
                Integer totalScore = computeTotalScoreByMatcher(matcherWV, mbtiSimilarity, hobbySimilarity, lifeStyleSimilarity, incomeSimilarity);
                String mainPhoto = matcher.getMainPhoto();
                Double distance = getDistance(userId, matched.getId());
                redisService.setStrValue(MATCHING_PREFIX + userId + "_" + matched.getId(), totalScore + "/" + mbtiSimilarity + "/" + hobbySimilarity + "/" + lifeStyleSimilarity + "/" + incomeSimilarity, MATCHING_DURATION);
                return MatchingSimpleResponse.builder()
                        .userId(matched.getId())
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

package hiff.hiff.behiff.domain.matching.util;

import static hiff.hiff.behiff.domain.matching.util.Calculator.computeIntAvg;
import static hiff.hiff.behiff.global.common.redis.RedisService.HOBBY_PREFIX;
import static hiff.hiff.behiff.global.common.redis.RedisService.INCOME_PREFIX;
import static hiff.hiff.behiff.global.common.redis.RedisService.LIFESTYLE_PREFIX;
import static hiff.hiff.behiff.global.common.redis.RedisService.MBTI_PREFIX;

import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.UserHobby;
import hiff.hiff.behiff.domain.user.domain.entity.UserLifeStyle;
import hiff.hiff.behiff.domain.user.infrastructure.UserHobbyRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserLifeStyleRepository;
import hiff.hiff.behiff.global.common.redis.RedisService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SimilarityFactory {

    private final UserHobbyRepository userHobbyRepository;
    private final RedisService redisService;
    private final UserLifeStyleRepository userLifeStyleRepository;


    public int getHobbySimilarity(User matcher, User matched) {
        List<UserHobby> matcherHobbies = userHobbyRepository.findByUserId(matcher.getId());
        List<UserHobby> matchedHobbies = userHobbyRepository.findByUserId(matched.getId());

        int hobbySimilaritySum = 0;
        for (UserHobby matcherHobby : matcherHobbies) {
            for (UserHobby matchedHobby : matchedHobbies) {
                String key =
                    HOBBY_PREFIX + matcherHobby.getHobbyId() + "_" + matchedHobby.getHobbyId();
                hobbySimilaritySum += redisService.getIntValue(key);
            }
        }
        return computeIntAvg(hobbySimilaritySum, matcherHobbies.size() * matchedHobbies.size());
    }

    public int getIncomeSimilarity(User matcher, User matched) {
        String key = INCOME_PREFIX + matcher.getIncome().getStartValue() + "_" + matched.getIncome()
            .getStartValue();
        return redisService.getIntValue(
            key);
    }

    public int getLifeStyleSimilarity(User matcher, User matched) {
        List<UserLifeStyle> matcherLifeStyles = userLifeStyleRepository.findByUserId(
            matcher.getId());
        List<UserLifeStyle> matchedLifeStyles = userLifeStyleRepository.findByUserId(
            matched.getId());

        int lifeStyleSimilaritySum = 0;
        for (UserLifeStyle matcherLifeStyle : matcherLifeStyles) {
            for (UserLifeStyle matchedLifeStyle : matchedLifeStyles) {
                lifeStyleSimilaritySum += redisService.getIntValue(
                    LIFESTYLE_PREFIX + matcherLifeStyle.getLifeStyleId() + "_"
                        + matchedLifeStyle.getLifeStyleId());
            }
        }
        return computeIntAvg(lifeStyleSimilaritySum,
            matcherLifeStyles.size() * matchedLifeStyles.size());
    }

    public int getMbtiSimilarity(User matcher, User matched) {
        String key = MBTI_PREFIX + matcher.getMbti() + "_" + matched.getMbti();
        return redisService.getIntValue(key);
    }
}

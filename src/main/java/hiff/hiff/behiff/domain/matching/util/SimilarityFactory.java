package hiff.hiff.behiff.domain.matching.util;

import static hiff.hiff.behiff.domain.catalog.application.service.CatalogHobbyService.HOBBY_PREFIX;
import static hiff.hiff.behiff.domain.catalog.application.service.CatalogLifeStyleService.LIFESTYLE_PREFIX;
import static hiff.hiff.behiff.domain.matching.util.Calculator.computeIntAvg;

import hiff.hiff.behiff.global.common.redis.RedisService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SimilarityFactory {

//    private final UserHobbyRepository userHobbyRepository;
    private final RedisService redisService;
//    private final UserLifeStyleRepository userLifeStyleRepository;


//    public int getHobbySimilarity(List<UserHobby> matcherHobbies, List<UserHobby> matchedHobbies) {
//        int hobbySimilaritySum = 0;
//        for (UserHobby matcherHobby : matcherHobbies) {
//            int max = 0;
//            for (UserHobby matchedHobby : matchedHobbies) {
//                String key =
//                    HOBBY_PREFIX + matcherHobby.getHobbyId() + "_" + matchedHobby.getHobbyId();
//                int similarity = redisService.getIntValue(key);
//                max = Math.max(max, similarity);
//            }
//            hobbySimilaritySum += max;
//        }
//        return computeIntAvg(hobbySimilaritySum, matcherHobbies.size());
//    }
//
//    public int getLifeStyleSimilarity(List<UserLifeStyle> matcherLifeStyles,
//        List<UserLifeStyle> matchedLifeStyles) {
//
//        int lifeStyleSimilaritySum = 0;
//        for (UserLifeStyle matcherLifeStyle : matcherLifeStyles) {
//            int max = 0;
//            for (UserLifeStyle matchedLifeStyle : matchedLifeStyles) {
//                String key =
//                    LIFESTYLE_PREFIX + matcherLifeStyle.getLifeStyleId() + "_"
//                        + matchedLifeStyle.getLifeStyleId();
//                int similarity = redisService.getIntValue(key);
//                max = Math.max(max, similarity);
//            }
//            lifeStyleSimilaritySum += max;
//        }

//        return computeIntAvg(lifeStyleSimilaritySum,
//            matcherLifeStyles.size());
//    }

//    public int getMbtiSimilarity(User matcher, User matched) {
//        String key = MBTI_PREFIX + matcher.getMbti() + "_" + matched.getMbti();
//
//        int similarity = redisService.getIntValue(key);
//        if (similarity == 0) {
//            key = MBTI_PREFIX + matched.getMbti() + "_" + matcher.getMbti();
//            similarity = redisService.getIntValue(key);
//        }
//        return similarity;
//    }
}

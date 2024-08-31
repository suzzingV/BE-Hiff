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
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SimilarityFactory {

    private final UserHobbyRepository userHobbyRepository;
    private final RedisService redisService;
    private final UserLifeStyleRepository userLifeStyleRepository;


    public int getHobbySimilarity(List<UserHobby> matcherHobbies, List<UserHobby> matchedHobbies) {
        Instant startTime, endTime;

//        startTime = Instant.now();
//        String matcherStr = redisService.getStrValue("ushob_" + matcher.getId());
//        String matchedStr = redisService.getStrValue("ushob_" + matched.getId());
//        StringTokenizer st1 = new StringTokenizer(matcherStr, "/");
//        StringTokenizer st2 = new StringTokenizer(matchedStr, "/");
//        List<Integer> matcherHobbies = new ArrayList<>(st1.countTokens());
//        List<Integer> matchedHobbies = new ArrayList<>(st2.countTokens());
//        while(st1.hasMoreTokens()) {
//            matcherHobbies.add(Integer.parseInt(st1.nextToken()));
//        }
//        while(st2.hasMoreTokens()) {
//            matchedHobbies.add(Integer.parseInt(st2.nextToken()));
//        }
//        endTime = Instant.now();
//        log.info("hobby select: {}", Duration.between(startTime, endTime).toMillis());
//
//        int hobbySimilaritySum = 0;
//        for (Integer matcherHobby : matcherHobbies) {
//            for (Integer matchedHobby : matchedHobbies) {
//                String key =
//                    HOBBY_PREFIX + matcherHobby + "_" + matchedHobby;
//                int similarity = redisService.getIntValue(key);
//                if(similarity == 0) {
//                    key = HOBBY_PREFIX + matchedHobby + "_" + matcherHobby;
//                    similarity = redisService.getIntValue(key);
//                }
//                hobbySimilaritySum += similarity;
//            }
//        }

        int hobbySimilaritySum = 0;
        for (UserHobby matcherHobby : matcherHobbies) {
            for (UserHobby matchedHobby : matchedHobbies) {
                String key =
                    HOBBY_PREFIX + matcherHobby.getHobbyId() + "_" + matchedHobby.getHobbyId();
                int similarity = redisService.getIntValue(key);
                if(similarity == 0) {
                    key = HOBBY_PREFIX + matchedHobby.getHobbyId() + "_" + matcherHobby.getHobbyId();
                    similarity = redisService.getIntValue(key);
                }
                hobbySimilaritySum += similarity;
            }
        }
        return computeIntAvg(hobbySimilaritySum, matcherHobbies.size() * matchedHobbies.size());
    }

//    public int getIncomeSimilarity(User matcher, User matched) {
//        String key = INCOME_PREFIX + matcher.getIncome().getStartValue() + "_" + matched.getIncome()
//            .getStartValue();
//        return redisService.getIntValue(
//            key);
//    }

    public int getLifeStyleSimilarity(List<UserLifeStyle> matcherLifeStyles, List<UserLifeStyle> matchedLifeStyles) {

        int lifeStyleSimilaritySum = 0;
        for (UserLifeStyle matcherLifeStyle : matcherLifeStyles) {
            for (UserLifeStyle matchedLifeStyle : matchedLifeStyles) {
                String key =
                    LIFESTYLE_PREFIX + matcherLifeStyle.getLifeStyleId() + "_"
                        + matchedLifeStyle.getLifeStyleId();
                int similarity = redisService.getIntValue(key);
                if(similarity == 0) {
                    key = LIFESTYLE_PREFIX + matchedLifeStyle.getLifeStyleId() + "_"
                        + matcherLifeStyle.getLifeStyleId();
                    similarity = redisService.getIntValue(key);
                }
                lifeStyleSimilaritySum += similarity;
            }
        }

//        startTime = Instant.now();
//        String matcherStr = redisService.getStrValue("uslif_" + matcher.getId());
//        String matchedStr = redisService.getStrValue("uslif_" + matched.getId());
//        StringTokenizer st1 = new StringTokenizer(matcherStr, "/");
//        StringTokenizer st2 = new StringTokenizer(matchedStr, "/");
//        List<Integer> matcherLifeStyles = new ArrayList<>(st1.countTokens());
//        List<Integer> matchedLifeStyles = new ArrayList<>(st2.countTokens());
//        while(st1.hasMoreTokens()) {
//            matcherLifeStyles.add(Integer.parseInt(st1.nextToken()));
//        }
//        while(st2.hasMoreTokens()) {
//            matchedLifeStyles.add(Integer.parseInt(st2.nextToken()));
//        }
//        endTime = Instant.now();
//        log.info("lifestyle select: {}", Duration.between(startTime, endTime).toMillis());
//
//        int lifeStyleSimilaritySum = 0;
//        for (Integer matcherLifeStyle : matcherLifeStyles) {
//            for (Integer matchedLifeStyle : matchedLifeStyles) {
//                String key =
//                    HOBBY_PREFIX + matcherLifeStyle + "_" + matchedLifeStyle;
//                int similarity = redisService.getIntValue(key);
//                if(similarity == 0) {
//                    key = HOBBY_PREFIX + matchedLifeStyle + "_" + matcherLifeStyle;
//                    similarity = redisService.getIntValue(key);
//                }
//                lifeStyleSimilaritySum += similarity;
//            }
//        }
        return computeIntAvg(lifeStyleSimilaritySum,
            matcherLifeStyles.size() * matchedLifeStyles.size());
    }

    public int getMbtiSimilarity(User matcher, User matched) {
        String key = MBTI_PREFIX + matcher.getMbti() + "_" + matched.getMbti();

        int similarity = redisService.getIntValue(key);
        if(similarity == 0) {
            key = MBTI_PREFIX + matched.getMbti() + "_" + matcher.getMbti();
            similarity = redisService.getIntValue(key);
        }
        return similarity;
    }
}

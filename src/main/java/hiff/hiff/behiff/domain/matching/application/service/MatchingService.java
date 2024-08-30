package hiff.hiff.behiff.domain.matching.application.service;

import static hiff.hiff.behiff.domain.matching.util.Calculator.computeDistance;
import static hiff.hiff.behiff.domain.matching.util.Calculator.computeTotalScoreByMatcher;
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
import hiff.hiff.behiff.domain.user.domain.entity.UserPos;
import hiff.hiff.behiff.domain.user.domain.entity.WeightValue;
import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
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
    private final UserPosService userPosService;
    private final RedisService redisService;
    private final MatchingRepository matchingRepository;
    private final SimilarityFactory similarityFactory;
    private final UserHobbyService userHobbyService;
    private final UserLifeStyleService userLifeStyleService;
    private final UserPhotoService userPhotoService;

    private static final Integer DAILY_MATCHING_HEART = 1;
    private static final Integer HIFF_MATCHING_HEART = 3;

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
        checkMatchingHistory(matcherId, matchedId);
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

    public void getNewHiffMatching(List<User> malesArr, List<User> femalesArr) {
        LocalDateTime start = LocalDateTime.now();

//        Collections.shuffle(malesArr);
        Collections.shuffle(femalesArr);
        LinkedList<User> males = new LinkedList<>(malesArr);
        PriorityQueue<UserWithMatchCount> females = new PriorityQueue<>();
        for (User female : femalesArr) {
            females.add(new UserWithMatchCount(female, 0));
        }

        for(User male : males) {
            List<UserWithMatchCount> tmp = new ArrayList<>();
            while(!females.isEmpty()) {
                UserWithMatchCount femaleWithCount = females.remove();
                tmp.add(femaleWithCount);
                User female = femaleWithCount.getUser();
                List<Long> matchingHistory = matchingRepository.findByUsers(female.getId(),
                        male.getId());
                    if(!matchingHistory.isEmpty()) {
                        continue;
                    }
                    if(female.getHopeMinAge() > male.getAge() || female.getHopeMaxAge() < male.getAge()
                        || male.getHopeMinAge() > female.getAge() || male.getHopeMaxAge() < female.getAge()) {
                        continue;
                    }
                    Double distance = getDistance(female.getId(), male.getId());
                    if(distance > male.getMaxDistance() || distance < male.getMinDistance()
                    || distance > female.getMaxDistance() || distance < female.getMinDistance()) {
                        continue;
                    }
                    WeightValue femaleWV = userWeightValueService.findByUserId(female.getId());
                    WeightValue maleWV = userWeightValueService.findByUserId(male.getId());
                    LocalDate today = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
                    String formattedDate = today.format(formatter);
                    MatchingInfoDto maleMatchingInfo = getNewMatchingInfo(male, female, maleWV);
                    MatchingInfoDto femaleMatchingInfo = getNewMatchingInfo(female, male, femaleWV);
                    if(maleMatchingInfo.getTotalScore() >= 80 && femaleMatchingInfo.getTotalScore() >= 80) {
//                        cachMatchingScore(female, male, femaleMatchingInfo, formattedDate + HIFF_MATCHING_PREFIX);
                        cachMatchingScore(male, female, maleMatchingInfo, formattedDate + HIFF_MATCHING_PREFIX);
                        recordMatchingHistory(female.getId(), male.getId());
                        recordMatchingHistory(male.getId(), female.getId());
                        femaleWithCount.increaseCount();
                        break;
                    }
                }
            females.addAll(tmp);
            }
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

    private void checkMatchingHistory(Long matcherId, Long matchedId) {
        matchingRepository.findByMatcherIdAndMatchedId(matcherId, matchedId)
            .orElseThrow(() -> new MatchingException(ErrorCode.MATCHING_HISTORY_NOT_FOUND));
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

    private MatchingInfoDto getNewMatchingInfo(User matcher, User matched,
        WeightValue matcherWV) {
        int mbtiSimilarity = similarityFactory.getMbtiSimilarity(matcher, matched);
        int hobbySimilarity = similarityFactory.getHobbySimilarity(matcher, matched);
        int lifeStyleSimilarity = similarityFactory.getLifeStyleSimilarity(matcher, matched);
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

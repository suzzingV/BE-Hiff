package hiff.hiff.behiff.domain.matching.application.service;

import static hiff.hiff.behiff.domain.matching.domain.enums.MatchingStatus.*;
import static hiff.hiff.behiff.domain.matching.util.Calculator.computeDistance;
import static hiff.hiff.behiff.global.common.redis.RedisService.NOT_EXIST;
import static hiff.hiff.behiff.global.util.DateCalculator.*;

import hiff.hiff.behiff.domain.bond.infrastructure.ChatRepository;
import hiff.hiff.behiff.domain.bond.infrastructure.LikeRepository;
import hiff.hiff.behiff.domain.matching.domain.entity.Matching;
import hiff.hiff.behiff.domain.matching.domain.enums.MatchingStatus;
import hiff.hiff.behiff.domain.matching.exception.MatchingException;
import hiff.hiff.behiff.domain.matching.infrastructure.MatchingRepository;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingDetailResponse;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingSimpleResponse;
import hiff.hiff.behiff.domain.plan.application.service.PlanService;
import hiff.hiff.behiff.domain.plan.presentation.dto.res.CouponResponse;
import hiff.hiff.behiff.domain.profile.application.dto.UserIntroductionDto;
import hiff.hiff.behiff.domain.profile.application.service.UserIntroductionService;
import hiff.hiff.behiff.domain.profile.application.service.UserPhotoService;
import hiff.hiff.behiff.domain.profile.application.service.UserPosService;
import hiff.hiff.behiff.domain.profile.application.service.UserProfileService;
import hiff.hiff.behiff.domain.profile.domain.entity.UserPos;
import hiff.hiff.behiff.domain.profile.domain.entity.UserProfile;
import hiff.hiff.behiff.domain.profile.exception.ProfileException;
import hiff.hiff.behiff.domain.profile.infrastructure.UserProfileRepository;
import hiff.hiff.behiff.domain.user.application.service.UserService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    private final UserPosService userPosService;
    private final RedisService redisService;
    private final MatchingRepository matchingRepository;
    private final UserPhotoService userPhotoService;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileService userProfileService;
    private final UserIntroductionService userIntroductionService;
    private final PlanService planService;
    private final ChatRepository chatRepository;
    private final LikeRepository likeRepository;
    private final UserService userService;

    public static final Duration MATCHING_DURATION = Duration.ofDays(1);
    public static final String MATCHING_PREFIX = "matching_";

    public List<MatchingSimpleResponse> getMatchings(Long userId) {
        return redisService.scanKeysByPattern(
                        MATCHING_PREFIX + TODAY_DATE + "??_" + userId)
                .stream()
                .filter(this::isValidMatchingData)
                .map(key -> {
                    Long matchedId = redisService.getLongValue(key);
                    UserProfile matched = userProfileService.findByUserId(matchedId);
                    return MatchingSimpleResponse.builder()
                            .age(matched.getAge())
                            .matchedId(matchedId)
                            .mainPhoto(matched.getMainPhoto())
                            .nickname(matched.getNickname())
                            .location(matched.getLocation())
                            .build();
                }).toList();
    }

    public MatchingDetailResponse getMatchingDetails(Long userId, Long matchedId) {
        if(!isMatchedBefore(userId, matchedId)) {
            throw new ProfileException(ErrorCode.MATCHING_NOT_FOUND);
        }
        UserProfile matchedProfile = userProfileService.findByUserId(matchedId);
        List<String> photos = userPhotoService.getPhotosOfUser(matchedId);
        List<UserIntroductionDto> introductions = userIntroductionService.findIntroductionByUserId(matchedId);
        MatchingStatus matchingStatus = getMatchingStatus(userId, matchedId);
        return getResponseByMatchingStatus(userId, matchedId, matchingStatus, matchedProfile, photos, introductions);
    }

    public void performMatching(UserProfile matcher) {
        userProfileRepository.getRandomMatched(matcher.getId())
                .forEach(matched -> {
//                Weighting matcherWV = userWeightValueService.findByUserId(matcher.getId());
//                List<UserHobby> matcherHobbies = userHobbyService.findByUserId(matcher.getId());
//                List<UserHobby> matchedHobbies = userHobbyService.findByUserId(matched.getId());
//                List<UserLifeStyle> matcherLifeStyles = userLifeStyleService.findByUserId(
//                    matcher.getId());
//                List<UserLifeStyle> matchedLifeStyles = userLifeStyleService.findByUserId(
//                    matched.getId());
//                MatchingInfoDto matchingInfoDto = getNewMatchingInfo(matcher, matched, matcherWV,
//                    matcherHobbies, matchedHobbies, matcherLifeStyles, matchedLifeStyles);

                    cachMatchingScore(matcher, matched);
                    LocalDate matchingDate = getMatchingDate();
                    createMatching(matcher.getUserId(), matched.getUserId(), matchingDate);
                });
    }

    private void cachMatchingScore(UserProfile matcher, UserProfile matched) {
        String matchingDate = getMatchingDateTimeByString();
        String prefix = MATCHING_PREFIX + matchingDate;
        String key = prefix + "_" + matcher.getUserId();
        Long value = matched.getUserId();
        redisService.setValue(key, value);
    }

    private boolean isValidMatchingData(String key) {
        StringTokenizer st = new StringTokenizer(key, "_");
        st.nextToken();
        String date = st.nextToken();
        int hour = Integer.parseInt(date.substring(date.length() - 2));
        int currentHour = LocalDateTime.now().getHour();

        return hour <= currentHour;
    }

    private MatchingStatus getMatchingStatus(Long userId, Long matchedId) {
        MatchingStatus status = findByUsers(userId, matchedId).getStatus();

        if(status == MatchingStatus.DEFAULT || status == MatchingStatus.MUTUAL_CHAT || status == MatchingStatus.MUTUAL_LIKE) {
            return status;
        }

        if(status == MatchingStatus.CHAT_PENDING) {
            return getChatStatus(userId, matchedId);
        }

        if(status == MatchingStatus.ONE_WAY_LIKE) {
            if(isLikeSenderOfResponder(userId, matchedId)) {
                return MatchingStatus.LIKE_PENDING;
            } else {
                return MatchingStatus.LIKE_RECEIVED;
            }
        }

        return status;
    }




    protected String getCachedValue(Long matcherId, Long matchedId, String prefix) {
        String key = prefix + matcherId + "_" + matchedId;
        log.info(prefix);
        String value = redisService.getStrValue(key);
        if (value.equals(NOT_EXIST)) {
            throw new MatchingException(ErrorCode.MATCHING_SCORE_NOT_FOUND);
        }
        return value;
    }

//    protected MatchingInfoDto getNewMatchingInfo(User matcher, User matched,
//                                                 UserWeighting matcherWV, List<UserHobby> matcherHobbies, List<UserHobby> matchedHobbies,
//                                                 List<UserLifeStyle> matcherLifeStyles, List<UserLifeStyle> matchedLifeStyles) {
////        int mbtiSimilarity = similarityFactory.getMbtiSimilarity(matcher, matched);
//        int hobbySimilarity = similarityFactory.getHobbySimilarity(matcherHobbies, matchedHobbies);
//        int lifeStyleSimilarity = similarityFactory.getLifeStyleSimilarity(matcherLifeStyles,
//            matchedLifeStyles);
////        int incomeSimilarity = similarityFactory.getIncomeSimilarity(matcher, matched);
////        Integer totalScore = computeTotalScoreByMatcher(matcherWV, mbtiSimilarity, hobbySimilarity,
////            lifeStyleSimilarity, matched.getEvaluatedScore());
//        return MatchingInfoDto.builder()
////            .mbtiSimilarity(mbtiSimilarity)
//            .hobbySimilarity(hobbySimilarity)
//            .lifeStyleSimilarity(lifeStyleSimilarity)
////            .incomeSimilarity(incomeSimilarity)
////            .totalScoreByMatcher(totalScore)
//            .build();
//    }

    protected void createMatching(Long userId, Long matchedId, LocalDate matchingDate) {
        Matching matching = Matching.builder()
            .matchedId(matchedId)
            .matcherId(userId)
                .createdAt(matchingDate)
            .build();
        log.info("상태: " + matching.getStatus().toString());
        matchingRepository.save(matching);
    }

    protected Double getDistance(Long matcherId, Long matchedId) {
        UserPos matcherPos = userPosService.findPosByUserId(matcherId);
        UserPos matchedPos = userPosService.findPosByUserId(matchedId);

        return computeDistance(matcherPos.getLat(), matcherPos.getLon(), matchedPos.getLat(),
            matchedPos.getLon());
    }

    public boolean isMatchedBefore(Long matcherId, Long matchedId) {
        Optional<Matching> matching = matchingRepository.findByUsers(matcherId, matchedId);
        return matching.isPresent();
    }

    public Matching findByUsers(Long userId, Long responderId) {
        return matchingRepository.findByUsers(userId, responderId)
                .orElseThrow(() -> new MatchingException(ErrorCode.MATCHING_NOT_FOUND));
    }

    private MatchingStatus getChatStatus(Long userId, Long matchedId) {
        return chatRepository.findByUsers(userId, matchedId)
                .map(chat -> {
                    if(chat.getSenderId().equals(userId)) {
                        return CHAT_PENDING;
                    }
                    return CHAT_RECEIVED;
                })
                .orElse(DEFAULT);
    }

    private boolean isLikeSenderOfResponder(Long userId, Long matchedId) {
        return likeRepository.findBySenderIdAndResponderId(userId, matchedId)
                .isPresent();
    }

    private MatchingDetailResponse getResponseByMatchingStatus(Long userId, Long matchedId, MatchingStatus matchingStatus, UserProfile matchedProfile, List<String> photos, List<UserIntroductionDto> introductions) {
        if(matchingStatus == MatchingStatus.MUTUAL_LIKE) {
            CouponResponse coupon = planService.getUserPlan(userId);
            return MatchingDetailResponse.of(userId, matchedProfile, photos, introductions, matchingStatus, coupon.getCoupon());
        }

        if(matchingStatus == MUTUAL_CHAT) {
            User user = userService.findById(matchedId);
            return MatchingDetailResponse.of(userId, matchedProfile, photos, introductions, matchingStatus, user.getPhoneNum());
        }
//        List<NameWithCommonDto> hobbies = userHobbyService.getHobbiesWithCommon(matcherId,
//            matchedId);
//        List<NameWithCommonDto> lifeStyles = userLifeStyleService.getLifeStylesWithCommon(matcherId,
//            matchedId);
//        Double distance = getDistance(matcherId, matchedId);
//        MatchingInfoDto matchingInfo = getCachedMatchingInfo(
//            matcherId, matchedId);

        return MatchingDetailResponse.of(userId, matchedProfile, photos, introductions, matchingStatus);
    }

//    protected Long getMatchedIdFromKey(String key) {
//        StringTokenizer st = new StringTokenizer(key, "_");
//        st.nextToken();
//        st.nextToken();
//        return Long.parseLong(st.nextToken());
//    }


}

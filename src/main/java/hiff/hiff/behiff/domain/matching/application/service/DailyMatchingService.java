package hiff.hiff.behiff.domain.matching.application.service;

import hiff.hiff.behiff.domain.bond.application.BondService;
import hiff.hiff.behiff.domain.bond.domain.Chat;
import hiff.hiff.behiff.domain.bond.domain.Like;
import hiff.hiff.behiff.domain.bond.infrastructure.ChatRepository;
import hiff.hiff.behiff.domain.bond.infrastructure.LikeRepository;
import hiff.hiff.behiff.domain.matching.domain.enums.MatchingStatus;
import hiff.hiff.behiff.domain.matching.infrastructure.MatchingRepository;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingDetailResponse;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingSimpleResponse;
import hiff.hiff.behiff.domain.matching.util.SimilarityFactory;
import hiff.hiff.behiff.domain.profile.application.dto.UserIntroductionDto;
import hiff.hiff.behiff.domain.profile.application.service.UserIntroductionService;
import hiff.hiff.behiff.domain.profile.application.service.UserPhotoService;
import hiff.hiff.behiff.domain.profile.application.service.UserPosService;
import hiff.hiff.behiff.domain.profile.application.service.UserProfileService;
import hiff.hiff.behiff.domain.profile.domain.entity.UserProfile;
import hiff.hiff.behiff.domain.profile.exception.ProfileException;
import hiff.hiff.behiff.domain.profile.infrastructure.UserProfileRepository;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import static hiff.hiff.behiff.global.util.DateCalculator.TODAY_DATE;
import static hiff.hiff.behiff.global.util.DateCalculator.getMatchingDate;

@Service
@Transactional
@Slf4j
public class DailyMatchingService extends MatchingService {

//    private final UserService userCRUDService;
//    private final UserRepository userRepository;
    private final RedisService redisService;
    private final UserPhotoService userPhotoService;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileService userProfileService;
    private final UserIntroductionService userIntroductionService;
    private final LikeRepository likeRepository;
    private final ChatRepository chatRepository;

//    public static final Duration MATCHING_DURATION = Duration.ofDays(1);
    public static final String MATCHING_PREFIX = "matching_";

    public DailyMatchingService(UserPosService userPosService, RedisService redisService,
                                MatchingRepository matchingRepository, SimilarityFactory similarityFactory, UserProfileRepository userProfileRepository, UserProfileService userProfileService, UserPhotoService userPhotoService, UserIntroductionService userIntroductionService, LikeRepository likeRepository, ChatRepository chatRepository) {
        super(userPosService, redisService, matchingRepository, similarityFactory);
//        this.userCRUDService = userService;
//        this.userRepository = userRepository;
        this.redisService = redisService;
        this.userProfileRepository = userProfileRepository;
        this.userProfileService = userProfileService;
        this.userPhotoService = userPhotoService;
        this.userIntroductionService = userIntroductionService;
        this.likeRepository = likeRepository;
        this.chatRepository = chatRepository;
    }

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
//
//    public List<MatchingSimpleResponse> getPaidMatching(Long userId) {
//        User matcher = userCRUDService.findById(userId);
//        List<String> matchingData = redisService.scanKeysWithPrefix(
//            PAID_DAILY_MATCHING_PREFIX + userId + "_");
//        List<MatchingSimpleResponse> newMatching = getNewMatching(matcher);
//
//        matchingData.forEach(redisService::delete);
//        return newMatching;
//    }
//
    public MatchingDetailResponse getMatchingDetails(Long userId, Long matchedId) {
        if(!isMatchedBefore(userId, matchedId)) {
            throw new ProfileException(ErrorCode.MATCHING_HISTORY_NOT_FOUND);
        }
        UserProfile matchedProfile = userProfileService.findByUserId(matchedId);
        List<String> photos = userPhotoService.getPhotosOfUser(matchedId);
        List<UserIntroductionDto> introductions = userIntroductionService.findIntroductionByUserId(matchedId);
        MatchingStatus matchingStatus = getMatchingStatus(userId, matchedId);
//        List<NameWithCommonDto> hobbies = userHobbyService.getHobbiesWithCommon(matcherId,
//            matchedId);
//        List<NameWithCommonDto> lifeStyles = userLifeStyleService.getLifeStylesWithCommon(matcherId,
//            matchedId);
//        Double distance = getDistance(matcherId, matchedId);
//        MatchingInfoDto matchingInfo = getCachedMatchingInfo(
//            matcherId, matchedId);

        return MatchingDetailResponse.of(userId, matchedProfile, photos, introductions, matchingStatus);
    }
//
//    private List<MatchingSimpleResponse> getSimpleMatchingInfo(Long userId,
//                                                               List<Long> matchedIds) {
//        return matchedIds.stream()
//            .map(matchedId -> {
////                Long matchedId = getMatchedIdFromKey(key);
//                UserProfile matched = userProfileService.findByUserId(matchedId);
//
//                return MatchingSimpleResponse.builder()
//                    .userId(matchedId)
//                    .age(matched.getAge())
//                    .nickname(matched.getNickname())
//                    .mainPhoto(matched.getMainPhoto())
//                    .matcherTotalScore(matchingInfo.getTotalScoreByMatcher())
//                    .distance(getDistance(userId, matchedId))
//                    .build();
//            }).toList();
//    }

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
                recordMatchingHistory(matcher.getUserId(), matched.getUserId());
            });
    }

//    private MatchingInfoDto getCachedMatchingInfo(Long matcherId, Long matchedId) {
//        String value = getCachedValue(matcherId, matchedId, DAILY_MATCHING_PREFIX);
//
//        StringTokenizer st = new StringTokenizer(value, "/");
//        int totalScore = Integer.parseInt(st.nextToken());
//        int mbtiSimilarity = Integer.parseInt(st.nextToken());
//        int hobbySimilarity = Integer.parseInt(st.nextToken());
//        int lifeStyleSimilarity = Integer.parseInt(st.nextToken());
////        int incomeSimilarity = Integer.parseInt(st.nextToken());
//
//        return MatchingInfoDto.builder()
//            .matcherId(matcherId)
//            .matchedId(matchedId)
//            .totalScoreByMatcher(totalScore)
//            .mbtiSimilarity(mbtiSimilarity)
//            .hobbySimilarity(hobbySimilarity)
////            .incomeSimilarity(incomeSimilarity)
//            .lifeStyleSimilarity(lifeStyleSimilarity)
//            .build();
//    }

    private void cachMatchingScore(UserProfile matcher, UserProfile matched) {
        String matchingDate = getMatchingDate();
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
        Optional<Chat> chatSentByUser = chatRepository.findBySenderIdAndResponderId(userId, matchedId);
        if(chatSentByUser.isPresent()) {
            if(chatSentByUser.get().getStatus() == MatchingStatus.MUTUAL_CHAT) {
                return MatchingStatus.MUTUAL_CHAT;
            } else if(chatSentByUser.get().getStatus() == MatchingStatus.CHAT_PENDING) {
                return MatchingStatus.CHAT_PENDING;
            }
        }

        Optional<Chat> chatSentByMatched = chatRepository.findBySenderIdAndResponderId(matchedId, userId);
        if(chatSentByMatched.isPresent()) {
            if(chatSentByMatched.get().getStatus() == MatchingStatus.MUTUAL_CHAT) {
                return MatchingStatus.MUTUAL_CHAT;
            } else if(chatSentByMatched.get().getStatus() == MatchingStatus.CHAT_PENDING) {
                return MatchingStatus.CHAT_RECEIVED;
            }
        }

        Optional<Like> likeSentByUser = likeRepository.findBySenderIdAndResponderId(userId, matchedId);
        Optional<Like> likeSentByMatched = likeRepository.findBySenderIdAndResponderId(matchedId, userId);
        if(likeSentByUser.isPresent() && likeSentByMatched.isPresent()) {
            return MatchingStatus.MUTUAL_LIKE;
        } else if(likeSentByUser.isPresent()) {
            return MatchingStatus.LIKE_PENDING;
        } else if(likeSentByMatched.isPresent()) {
            return MatchingStatus.LIKE_RECEIVED;
        }
        return MatchingStatus.INIT;
    }
}

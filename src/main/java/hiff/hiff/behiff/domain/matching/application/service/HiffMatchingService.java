//package hiff.hiff.behiff.domain.matching.application.service;
//
//import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
//import org.springframework.stereotype.Service;
//
//@Service
//@Transactional
//@Slf4j
//public class HiffMatchingService extends MatchingService {
//
//    private final UserCRUDService userCRUDService;
//    private final UserWeightValueService userWeightValueService;
//    private final UserRepository userRepository;
//    private final UserPosService userPosService;
//    private final RedisService redisService;
//    private final UserHobbyService userHobbyService;
//    private final UserHobbyRepository userHobbyRepository;
//    private final UserLifeStyleRepository userLifeStyleRepository;
//    private final UserLifeStyleService userLifeStyleService;
//    private final UserPhotoService userPhotoService;
//    private final ChatHistoryRepository chatHistoryRepository;
//
//    private static final Integer HIFF_MATCHING_HEART = 3;
//    private static final int TOTAL_SCORE_STANDARD = 70;
//    public static final Duration DAILY_HIFF_MATCHING_DURATION = Duration.ofDays(2);
//    public static final String HIFF_MATCHING_PREFIX = "hiff_";
//
//    public HiffMatchingService(UserPosService userPosService, RedisService redisService,
//        MatchingRepository matchingRepository, SimilarityFactory similarityFactory,
//        UserCRUDService userCRUDService, UserWeightValueService userWeightValueService,
//        UserRepository userRepository, UserPosService userPosService1, RedisService redisService1,
//        UserHobbyService userHobbyService, UserHobbyRepository userHobbyRepository,
//        UserLifeStyleRepository userLifeStyleRepository, UserLifeStyleService userLifeStyleService,
//        UserPhotoService userPhotoService, ChatHistoryRepository chatHistoryRepository) {
//        super(userPosService, redisService, matchingRepository, similarityFactory);
//        this.userCRUDService = userCRUDService;
//        this.userWeightValueService = userWeightValueService;
//        this.userRepository = userRepository;
//        this.userPosService = userPosService1;
//        this.redisService = redisService1;
//        this.userHobbyService = userHobbyService;
//        this.userHobbyRepository = userHobbyRepository;
//        this.userLifeStyleRepository = userLifeStyleRepository;
//        this.userLifeStyleService = userLifeStyleService;
//        this.userPhotoService = userPhotoService;
//        this.chatHistoryRepository = chatHistoryRepository;
//    }
//
//    public List<MatchingSimpleResponse> getMatchings(Long userId) {
////        String today = getTodayDate();
//        List<String> originalMatching = redisService.scanKeysWithPrefix(
//            HIFF_MATCHING_PREFIX + userId + "_");
//        return getCachedMatching(userId, originalMatching);
//    }
//
//    public Long performMatching(Long userId) {
//        User user = userCRUDService.findById(userId);
//        List<User> matcheds = userRepository.getMatched(userId, user.getGender());
//        Collections.shuffle(matcheds);
//        Queue<User> matchedQueue = new LinkedList<>(matcheds);
//
//        UserPos matcherPos = userPosService.findPosByUserId(userId);
//        Weighting matcherWV = userWeightValueService.findByUserId(userId);
//        List<UserHobby> matcherHobbies = userHobbyRepository.findByUserId(userId);
//        List<UserLifeStyle> matcherLifeStyles = userLifeStyleRepository.findByUserId(userId);
//
//        while (!matchedQueue.isEmpty()) {
//            User matched = matchedQueue.remove();
//            log.info("matched: " + matched.getId());
//            if (matched.getEvaluatedScore() == 0 || matched.getNickname() == null) {
//                log.info("외모 점수");
//                continue;
//            }
//
//            if (checkMatchingHistory(matched.getId())) {
//                log.info("매칭기록");
//                continue;
//            }
//
//            if (checkAge(user, matched)) {
//                log.info("나이");
//                continue;
//            }
//
//            UserPos matchedPos = userPosService.findPosByUserId(matched.getId());
//            Double distance = computeDistance(matcherPos.getLat(), matcherPos.getLon(),
//                matchedPos.getLat(),
//                matchedPos.getLon());
//            if (checkDistance(user, matched, distance)) {
//                log.info("거리");
//                continue;
//            }
//
//            Weighting matchedWV = userWeightValueService.findByUserId(matched.getId());
//            List<UserHobby> matchedHobbies = userHobbyRepository.findByUserId(matched.getId());
//            List<UserLifeStyle> matchedLifeStyle = userLifeStyleRepository.findByUserId(
//                matched.getId());
//            MatchingInfoDto userMatchingInfo = getNewMatchingInfo(user, matched, matcherWV,
//                matcherHobbies, matchedHobbies, matcherLifeStyles, matchedLifeStyle);
//            MatchingInfoDto matchedMatchingInfo = getNewMatchingInfo(matched, user, matchedWV,
//                matchedHobbies, matcherHobbies, matchedLifeStyle, matcherLifeStyles);
//
//            log.info(userId + " " + matched.getId() + " 총 점수: "
//                + userMatchingInfo.getTotalScoreByMatcher() + " "
//                + matchedMatchingInfo.getTotalScoreByMatcher());
//            if (checkTotalScore(userMatchingInfo, matchedMatchingInfo)) {
//                String today = getTodayDate();
//                cachMatchingScore(userId, matched.getId(), userMatchingInfo,
//                    matchedMatchingInfo.getTotalScoreByMatcher(), today, MATCHING_DURATION);
////                recordMatchingHistory(matched.getId(), userId);
////                recordMatchingHistory(userId, matched.getId());
//                return matched.getId();
//            }
//        }
//
//        return null;
//    }
//
//    public HiffMatchingDetailResponse getMatchingDetails(Long matcherId, Long matchedId) {
////        if (!isMatchedBefore(matcherId, matchedId)) {
////            throw new MatchingException(ErrorCode.MATCHING_HISTORY_NOT_FOUND);
////        }
//        User matcher = userCRUDService.findById(matcherId);
//        User matched = userCRUDService.findById(matchedId);
//
//        String mainPhoto = matched.getMainPhoto();
//        List<String> photos = userPhotoService.getPhotosOfUser(matchedId);
//        List<NameWithCommonDto> hobbies = userHobbyService.getHobbiesWithCommon(matcherId,
//            matchedId);
//        List<NameWithCommonDto> lifeStyles = userLifeStyleService.getLifeStylesWithCommon(matcherId,
//            matchedId);
//
//        Double distance = getDistance(matcherId, matchedId);
//        MatchingInfoDto matchingInfoDto = getCachedMatchingInfo(matcherId, matchedId);
//
//        boolean isPropose = hasProposed(matcherId, matchedId);
//        boolean isProposed = !isPropose && hasProposed(matchedId, matcherId);
//
//        return HiffMatchingDetailResponse.of(matcher, matched, distance, mainPhoto, photos,
//            matchingInfoDto, hobbies, lifeStyles, isPropose, isProposed);
//    }
//
//    public void voiddailyMatching(User matcher, PriorityQueue<UserWithMatchCount> matchedArr) {
//        UserPos matcherPos = userPosService.findPosByUserId(matcher.getId());
//        Weighting matcherWV = userWeightValueService.findByUserId(matcher.getId());
//        List<UserHobby> matcherHobbies = userHobbyRepository.findByUserId(matcher.getId());
//        List<UserLifeStyle> matcherLifeStyles = userLifeStyleRepository.findByUserId(
//            matcher.getId());
//        PriorityQueue<UserWithMatchCount> tmp = new PriorityQueue<>();
//
//        while (!matchedArr.isEmpty()) {
//            UserWithMatchCount matchedWithCount = matchedArr.remove();
//            User matched = matchedWithCount.getUser();
//
//            if (checkAge(matcher, matched)) {
//                tmp.add(matchedWithCount);
//                continue;
//            }
//
//            if (isMatchedBefore(matcher.getId(), matched.getId())) {
//                tmp.add(matchedWithCount);
//                continue;
//            }
//
//            UserPos matchedPos = userPosService.findPosByUserId(matched.getId());
//            Double distance = computeDistance(matcherPos.getLat(), matcherPos.getLon(),
//                matchedPos.getLat(),
//                matchedPos.getLon());
//            if (checkDistance(matcher, matched, distance)) {
//                tmp.add(matchedWithCount);
//                continue;
//            }
//
//            Weighting matchedWV = userWeightValueService.findByUserId(matched.getId());
//            List<UserHobby> matchedHobbies = userHobbyRepository.findByUserId(matched.getId());
//            List<UserLifeStyle> matchedLifeStyle = userLifeStyleRepository.findByUserId(
//                matched.getId());
//            MatchingInfoDto matcherMatchingInfo = getNewMatchingInfo(matcher, matched, matcherWV,
//                matcherHobbies, matchedHobbies, matcherLifeStyles, matchedLifeStyle);
//            MatchingInfoDto matchedMatchingInfo = getNewMatchingInfo(matched, matcher, matchedWV,
//                matchedHobbies, matcherHobbies, matchedLifeStyle, matcherLifeStyles);
//
//            if (checkTotalScore(matcherMatchingInfo, matchedMatchingInfo)) {
//                matchedWithCount.increaseCount();
//                tmp.add(matchedWithCount);
//
//                String tomorrow = getTomorrowDate();
//                cachMatchingScore(matcher.getId(), matched.getId(), matcherMatchingInfo,
//                    matchedMatchingInfo.getTotalScoreByMatcher(), tomorrow,
//                    DAILY_HIFF_MATCHING_DURATION);
//                recordMatchingHistory(matched.getId(), matcher.getId());
//                recordMatchingHistory(matcher.getId(), matched.getId());
//                break;
//            }
//            tmp.add(matchedWithCount);
//        }
//        matchedArr.addAll(tmp);
//        matchedQueue = matchedArr;
//    }
//
//    private MatchingInfoDto getCachedMatchingInfo(Long matcherId, Long matchedId) {
//        String today = getTodayDate();
//        String value = getCachedValue(matcherId, matchedId, HIFF_MATCHING_PREFIX);
//
//        StringTokenizer st = new StringTokenizer(value, "/");
//        int totalScoreByMatcher = Integer.parseInt(st.nextToken());
//        int totalScoreByMatched = Integer.parseInt(st.nextToken());
//        int mbtiSimilarity = Integer.parseInt(st.nextToken());
//        int hobbySimilarity = Integer.parseInt(st.nextToken());
//        int lifeStyleSimilarity = Integer.parseInt(st.nextToken());
////        int incomeSimilarity = Integer.parseInt(st.nextToken());
//
//        return MatchingInfoDto.builder()
//            .matcherId(matcherId)
//            .matchedId(matchedId)
//            .totalScoreByMatcher(totalScoreByMatcher)
//            .totalScoreByMatched(totalScoreByMatched)
//            .mbtiSimilarity(mbtiSimilarity)
//            .hobbySimilarity(hobbySimilarity)
////            .incomeSimilarity(incomeSimilarity)
//            .lifeStyleSimilarity(lifeStyleSimilarity)
//            .build();
//    }
//
//    private void cachMatchingScore(Long matcherId, Long matchedId, MatchingInfoDto matchingInfoDto,
//        int matchedTotalScore, String date, Duration duration) {
////        String prefix = date + HIFF_MATCHING_PREFIX;
//        String key = HIFF_MATCHING_PREFIX + matcherId + "_" + matchedId;
//        String value = matchingInfoDto.getTotalScoreByMatcher() + "/" + matchedTotalScore + "/"
//            + matchingInfoDto.getMbtiSimilarity() + "/"
//            + matchingInfoDto.getHobbySimilarity() + "/"
//            + matchingInfoDto.getLifeStyleSimilarity();
//        redisService.setValue(key, value, duration);
//
//        key = HIFF_MATCHING_PREFIX + matchedId + "_" + matcherId;
//        value = matchedTotalScore + "/" + matchingInfoDto.getTotalScoreByMatcher() + "/" +
//            +matchingInfoDto.getMbtiSimilarity() + "/"
//            + matchingInfoDto.getHobbySimilarity() + "/"
//            + matchingInfoDto.getLifeStyleSimilarity();
//        redisService.setValue(key, value, duration);
//    }
//
//    private List<MatchingSimpleResponse> getCachedMatching(Long userId,
//        List<String> originalMatching) {
//        return originalMatching.stream()
//            .map(key -> {
//                Long matchedId = getMatchedIdFromKey(key);
//                log.info(matchedId + "");
//                User matched = userCRUDService.findById(matchedId);
//                MatchingInfoDto matchingInfo = getCachedMatchingInfo(userId,
//                    matchedId);
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
//
//    private boolean checkTotalScore(MatchingInfoDto matcherMatchingInfo,
//        MatchingInfoDto matchedMatchingInfo) {
//        return matcherMatchingInfo.getTotalScoreByMatcher() >= TOTAL_SCORE_STANDARD
//            && matchedMatchingInfo.getTotalScoreByMatcher() >= TOTAL_SCORE_STANDARD;
//    }
//
//    private boolean hasProposed(Long proposerId, Long proposedId) {
//        return chatHistoryRepository.findByProposerIdAndProposedId(proposerId, proposedId)
//            .isPresent();
//    }
//
//    public boolean checkMatchingHistory(Long matchedId) {
//        return !redisService.keys(HIFF_MATCHING_PREFIX + matchedId + "*").isEmpty();
//    }
//}

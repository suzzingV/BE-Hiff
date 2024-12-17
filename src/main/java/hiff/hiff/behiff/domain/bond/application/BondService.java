package hiff.hiff.behiff.domain.bond.application;

import hiff.hiff.behiff.domain.bond.domain.Chat;
import hiff.hiff.behiff.domain.bond.domain.Like;
import hiff.hiff.behiff.domain.bond.exception.BondException;
import hiff.hiff.behiff.domain.bond.infrastructure.ChatRepository;
import hiff.hiff.behiff.domain.bond.infrastructure.LikeRepository;
import hiff.hiff.behiff.domain.bond.presentation.dto.res.ChatAcceptanceResponse;
import hiff.hiff.behiff.domain.bond.presentation.dto.res.ChatSendingResponse;
import hiff.hiff.behiff.domain.bond.presentation.dto.res.LikeResponse;
import hiff.hiff.behiff.domain.bond.presentation.dto.res.LikeToUserResponse;
import hiff.hiff.behiff.domain.matching.application.service.MatchingService;
import hiff.hiff.behiff.domain.matching.domain.entity.Matching;
import hiff.hiff.behiff.domain.matching.domain.enums.MatchingStatus;
import hiff.hiff.behiff.domain.plan.application.service.PlanService;
import hiff.hiff.behiff.domain.plan.domain.entity.UserPlan;
import hiff.hiff.behiff.domain.profile.application.service.UserProfileService;
import hiff.hiff.behiff.domain.profile.domain.entity.UserProfile;
import hiff.hiff.behiff.domain.user.application.service.UserService;
import hiff.hiff.behiff.global.auth.domain.entity.Device;
import hiff.hiff.behiff.global.common.fcm.FcmUtils;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static hiff.hiff.behiff.global.util.DateCalculator.getDateByPattern;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BondService {


//    private final AuthService authService;
    private final ChatRepository chatRepository;
//    private final UserCRUDService userCRUDService;
//    private final SmsUtil smsUtil;
    private final RedisService redisService;
    private final LikeRepository likeRepository;
    private final PlanService planService;
    private final MatchingService matchingService;
    private final FcmUtils fcmUtils;
    private final UserService userService;
    private final UserProfileService userProfileService;

    private final static String BOND_MATCHING_DATE_PATTERN = "MM.dd";

    public LikeResponse sendLike(Long userId, Long responderId) {
        isLikeBefore(userId, responderId);
        saveLikePendingStatus(userId, responderId);

        createLike(userId, responderId);

//        sendLikeSendAlarm(userId);

        return LikeResponse.of(userId, responderId);
    }

    public ChatSendingResponse sendChat(Long userId, Long responderId) {
        isChatBefore(userId, responderId);
        saveChatPendingStatus(userId, responderId);

        createChat(userId, responderId);
        useCoupon(userId);

//        sendChatSendAlarm(userId);

        return ChatSendingResponse.from(responderId);
    }

    public ChatAcceptanceResponse acceptChat(Long userId, Long senderId) {
        findChatBySenderIdAndResponderId(senderId, userId);
        saveChatAcceptStatus(userId, senderId);

//        sendChatAcceptAlarm(userId);
        return ChatAcceptanceResponse.from(senderId);
    }

    public List<LikeToUserResponse> getUsersOfSendingLike(Long userId) {
        return likeRepository.findBySenderId(userId)
                .stream().map(like -> {
                    Long responderId = like.getResponderId();
                    Matching matching = matchingService.findByUsers(userId, responderId);
                    UserProfile matchedProfile = userProfileService.findByUserId(responderId);

                    return LikeToUserResponse.of(responderId, matchedProfile.getNickname(), matchedProfile.getAge(), matchedProfile.getMainPhoto(), getDateByPattern(matching.getCreatedAt(), BOND_MATCHING_DATE_PATTERN));
                }).toList();
    }

    private void createChat(Long userId, Long responderId) {
        Chat chat = Chat.builder()
                .senderId(userId)
                .responderId(responderId)
                .build();
        chatRepository.save(chat);
    }

    private void saveChatPendingStatus(Long userId, Long responderId) {
        Matching matching = matchingService.findByUsers(userId, responderId);
        isMutualLike(matching);
        matching.sendChat();
    }

    private void saveChatAcceptStatus(Long userId, Long senderId) {
        Matching matching = matchingService.findByUsers(userId, senderId);
        matching.acceptChat();
    }

    private void isMutualLike(Matching matching) {
        if(matching.getStatus() != MatchingStatus.MUTUAL_LIKE) {
            throw new BondException(ErrorCode.NOT_MUTUAL_LIKE);
        }
    }

    private void createLike(Long userId, Long responderId) {
        Like like = Like.builder()
                .senderId(userId)
                .responderId(responderId)
                .build();
        likeRepository.save(like);
    }

    private void saveLikePendingStatus(Long userId, Long responderId) {
        Matching matching = matchingService.findByUsers(userId, responderId);
        matching.sendLike();
    }

    private void useCoupon(Long userId) {
        if(planService.isMembership(userId)) {
            return;
        }

        UserPlan userPlan = planService.findByUserId(userId);
        if(userPlan.getCoupon() <= 0) {
            throw new BondException(ErrorCode.LACK_OF_COUPON);
        }
        userPlan.subtractCoupon();
    }

    private void isChatBefore(Long userId, Long responderId) {
        Optional<Chat> chatHistory = chatRepository.findByUsers(userId, responderId);
        if(chatHistory.isPresent()) {
            throw new BondException(ErrorCode.CHAT_ALREADY_EXISTS);
        }
    }

    private void isLikeBefore(Long userId, Long responderId) {
        if(likeRepository.findBySenderIdAndResponderId(userId, responderId).isPresent()) {
            throw new BondException(ErrorCode.LIKE_ALREADY_EXISTS);
        }
    }

    private Chat findChatBySenderIdAndResponderId(Long senderId, Long responderId) {
        return chatRepository.findBySenderIdAndResponderId(senderId, responderId)
                .orElseThrow(() -> new BondException(ErrorCode.CHAT_NOT_FOUND));
    }

    private void sendChatSendAlarm(Long userId) {
        Device device = userService.findDeviceByUserId(userId);
        UserProfile userProfile = userProfileService.findByUserId(userId);
        fcmUtils.sendChatAlarm(userId, device.getFcmToken(), userProfile.getNickname());
    }

    private void sendLikeSendAlarm(Long userId) {
        Device device = userService.findDeviceByUserId(userId);
        UserProfile userProfile = userProfileService.findByUserId(userId);
        fcmUtils.sendLikeAlarm(userId, device.getFcmToken(), userProfile.getNickname());
    }

    private void sendChatAcceptAlarm(Long userId) {
        Device device = userService.findDeviceByUserId(userId);
        UserProfile userProfile = userProfileService.findByUserId(userId);
        fcmUtils.sendChatAcceptAlarm(userId, device.getFcmToken(), userProfile.getNickname());
    }
//    public ChatProposalResponse proposeChat(Long userId, Long matchedId) {
//        User user = userCRUDService.findById(userId);
//        Token token = authService.findTokenByUserId(matchedId);
//        recordChatHistory(user, matchedId);
//        fcmUtils.sendChatProposal(userId, token.getFcmToken(), user.getNickname());
////        User matched = userCRUDService.findById(matchedId);
////        smsUtil.sendProposeMessage(matched.getPhoneNum(), user.getNickname());
//        return ChatProposalResponse.builder()
//            .proposedId(matchedId)
//            .proposerId(user.getId())
//            .build();
//    }
//
//    public List<ChatProposerResponse> getProposedList(Long userId) {
//        return chatHistoryRepository.findByProposedId(userId)
//            .stream().map(chatHistory -> {
//                User proposer = userCRUDService.findById(chatHistory.getProposerId());
//                return ChatProposerResponse.builder()
//                    .proposerId(proposer.getId())
//                    .proposerNickname(proposer.getNickname())
//                    .isAccepted(chatHistory.getIsAccepted())
//                    .build();
//            })
//            .toList();
//    }
//
//    public List<ChatProposedResponse> getProposalList(Long userId) {
//        return chatHistoryRepository.findByProposerId(userId)
//            .stream().map(chatHistory -> {
//                User proposed = userCRUDService.findById(chatHistory.getProposedId());
//                return ChatProposedResponse.builder()
//                    .proposedId(proposed.getId())
//                    .proposedNickname(proposed.getNickname())
//                    .isAccepted(chatHistory.getIsAccepted())
//                    .build();
//            })
//            .toList();
//    }
//
//    public ChatProposalResponse acceptProposal(Long userId, ChatAcceptanceRequest request) {
//        User user = userCRUDService.findById(userId);
//        Long proposerId = request.getProposerId();
//        updateHistory(proposerId, user.getId());
//
//        User proposer = userCRUDService.findById(proposerId);
//        smsUtil.sendAcceptMessage(proposer.getPhoneNum(), user.getNickname(), user.getPhoneNum());
//
//        return ChatProposalResponse.builder()
//            .proposerId(proposerId)
//            .proposedId(user.getId()).build();
//    }
//
//    private void updateHistory(Long proposerId, Long userId) {
//        ChatHistory chatHistory = chatHistoryRepository.findByProposerIdAndProposedId(proposerId,
//                userId)
//            .orElseThrow(() -> new ChatException(ErrorCode.CHAT_HISTORY_NOT_FOUND));
//        chatHistory.accept();
//    }
//
//    private void recordChatHistory(User user, Long matchedId) {
//        // TODO: 테스트용
//        if (chatHistoryRepository.findByProposerIdAndProposedId(user.getId(), matchedId)
//            .isPresent()) {
//            return;
//        }
//        ChatHistory chatHistory = ChatHistory.builder()
//            .proposerId(user.getId())
//            .proposedId(matchedId)
//            .build();
//        chatHistoryRepository.save(chatHistory);
//    }
}

package hiff.hiff.behiff.domain.bond.application;

import hiff.hiff.behiff.domain.bond.domain.Chat;
import hiff.hiff.behiff.domain.bond.domain.Like;
import hiff.hiff.behiff.domain.bond.exception.BondException;
import hiff.hiff.behiff.domain.bond.infrastructure.ChatRepository;
import hiff.hiff.behiff.domain.bond.infrastructure.LikeRepository;
import hiff.hiff.behiff.domain.bond.presentation.dto.res.ChatSendingResponse;
import hiff.hiff.behiff.domain.bond.presentation.dto.res.LikeResponse;
import hiff.hiff.behiff.domain.matching.application.service.MatchingService;
import hiff.hiff.behiff.domain.matching.domain.enums.MatchingStatus;
import hiff.hiff.behiff.domain.plan.application.service.PlanService;
import hiff.hiff.behiff.domain.plan.domain.entity.UserPlan;
import hiff.hiff.behiff.domain.plan.exception.PlanException;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static hiff.hiff.behiff.global.common.redis.RedisService.NOT_EXIST;

@Service
@Transactional
@RequiredArgsConstructor
public class BondService {


//    private final AuthService authService;
    private final ChatRepository chatRepository;
//    private final UserCRUDService userCRUDService;
//    private final SmsUtil smsUtil;
//    private final FcmUtils fcmUtils;
    private final RedisService redisService;
    private final LikeRepository likeRepository;
    private final PlanService planService;
    private final MatchingService matchingService;

    public LikeResponse sendLike(Long userId, Long responderId) {
        if(!likeRepository.findBySenderIdAndResponderId(userId, responderId).isEmpty()) {
            throw new BondException(ErrorCode.LIKE_ALREADY_EXISTS);
        }

        Like like = Like.builder()
                .senderId(userId)
                .responderId(responderId)
                .build();
        likeRepository.save(like);

        return LikeResponse.of(userId, responderId);
    }

    public ChatSendingResponse sendChat(Long userId, Long responderId) {
        checkMatchingHistory(userId, responderId);
        checkLikeStatus(userId, responderId);
        checkChatHistory(userId, responderId);

        Chat chat = Chat.builder()
                .senderId(userId)
                .responderId(responderId)
                .status(MatchingStatus.CHAT_PENDING)
                .build();
        chatRepository.save(chat);

        useCoupon(userId);

        return ChatSendingResponse.from(responderId);
    }

    private void checkLikeStatus(Long userId, Long responderId) {
        if(getLikeStatus(userId, responderId) != MatchingStatus.MUTUAL_LIKE) {
            throw new BondException(ErrorCode.NOT_MUTUAL_LIKE);
        }
    }

    public MatchingStatus getLikeStatus(Long userId, Long matchedId) {
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

    public MatchingStatus getChatStatus(Long senderId, Long responderId) {
        Optional<Chat> chat = chatRepository.findBySenderIdAndResponderId(senderId, responderId);
        if(chat.isPresent()) {
            if(chat.get().getStatus() == MatchingStatus.MUTUAL_CHAT) {
                return MatchingStatus.MUTUAL_CHAT;
            } else if(chat.get().getStatus() == MatchingStatus.CHAT_PENDING) {
                return MatchingStatus.CHAT_PENDING;
            }
        }
        return null;
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

    private void checkChatHistory(Long userId, Long responderId) {
        Optional<Chat> chatHistory = chatRepository.findByUsers(userId, responderId);
        if(chatHistory.isPresent()) {
            throw new BondException(ErrorCode.CHAT_ALREADY_EXISTS);
        }
    }

    private void checkMatchingHistory(Long userId, Long responderId) {
        if(!matchingService.isMatchedBefore(userId, responderId)) {
            throw new BondException(ErrorCode.MATCHING_HISTORY_NOT_FOUND);
        }
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

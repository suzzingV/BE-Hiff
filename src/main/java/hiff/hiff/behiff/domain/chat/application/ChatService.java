package hiff.hiff.behiff.domain.chat.application;

import hiff.hiff.behiff.domain.chat.domain.ChatHistory;
import hiff.hiff.behiff.domain.chat.infrastructure.ChatHistoryRepository;
import hiff.hiff.behiff.domain.chat.presentation.dto.req.ChatAcceptanceRequest;
import hiff.hiff.behiff.domain.chat.presentation.dto.res.ChatProposalResponse;
import hiff.hiff.behiff.domain.chat.presentation.dto.res.ChatProposedResponse;
import hiff.hiff.behiff.domain.chat.presentation.dto.res.ChatProposerResponse;
import hiff.hiff.behiff.domain.user.application.UserCRUDService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.global.auth.domain.FcmToken;
import hiff.hiff.behiff.global.auth.infrastructure.FcmTokenRepository;
import hiff.hiff.behiff.global.common.fcm.FcmUtils;
import hiff.hiff.behiff.global.common.sms.SmsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final FcmTokenRepository fcmTokenRepository;
    private final ChatHistoryRepository chatHistoryRepository;
    private final UserCRUDService userCRUDService;
    private final SmsUtil smsUtil;

    public ChatProposalResponse proposeChat(User user, Long matchedId) {
        FcmToken matchedFcmToken = fcmTokenRepository.findByUserId(matchedId);
        recordChatHistory(user, matchedId);
        FcmUtils.sendChatProposal(matchedFcmToken.getToken(), user.getNickname());

        return ChatProposalResponse.builder()
                .proposedId(matchedId)
                .proposerId(user.getId())
                .build();
    }

    public List<ChatProposerResponse> getProposedList(Long userId) {
        return chatHistoryRepository.findByProposedId(userId)
                .stream().map(chatHistory -> {
                    User proposer = userCRUDService.findById(chatHistory.getProposerId());
                    return ChatProposerResponse.builder()
                            .proposerId(proposer.getId())
                            .proposerNickname(proposer.getNickname())
                            .isAccepted(chatHistory.getIsAccepted())
                            .build();
                })
                .toList();
    }

    public List<ChatProposedResponse> getProposalList(Long userId) {
        return chatHistoryRepository.findByProposerId(userId)
                .stream().map(chatHistory -> {
                    User proposed = userCRUDService.findById(chatHistory.getProposedId());
                    return ChatProposedResponse.builder()
                            .proposedId(proposed.getId())
                            .proposedNickname(proposed.getNickname())
                            .isAccepted(chatHistory.getIsAccepted())
                            .build();
                })
                .toList();
    }

    public ChatProposalResponse acceptProposal(User user, ChatAcceptanceRequest request) {
        Long proposerId = request.getProposerId();
        updateHistory(proposerId, user.getId());

        User proposer = userCRUDService.findById(proposerId);
        smsUtil.sendAcceptMessage(proposer.getPhoneNum(), user.getNickname(), user.getPhoneNum());

        return ChatProposalResponse.builder()
                .proposerId(proposerId)
                .proposedId(user.getId()).build();
    }

    private void updateHistory(Long proposerId, Long userId) {
        ChatHistory chatHistory = chatHistoryRepository.findByProposerIdAndProposedId(proposerId, userId);
        chatHistory.accept();
    }

    private void recordChatHistory(User user, Long matchedId) {
        ChatHistory chatHistory = ChatHistory.builder()
                .proposerId(user.getId())
                .proposedId(matchedId)
                .build();
        chatHistoryRepository.save(chatHistory);
    }
}

package hiff.hiff.behiff.domain.chat.application;

import hiff.hiff.behiff.domain.chat.domain.ChatHistory;
import hiff.hiff.behiff.domain.chat.infrastructure.ChatHistoryRepository;
import hiff.hiff.behiff.domain.chat.presentation.dto.res.ChatProposalResponse;
import hiff.hiff.behiff.domain.chat.presentation.dto.res.ChatProposedResponse;
import hiff.hiff.behiff.domain.user.application.UserCRUDService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.global.auth.domain.FcmToken;
import hiff.hiff.behiff.global.auth.infrastructure.FcmTokenRepository;
import hiff.hiff.behiff.global.common.fcm.FcmUtils;
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

    public void proposeChat(User user, Long matchedId) {
        FcmToken matchedFcmToken = fcmTokenRepository.findByUserId(matchedId);
        recordChatHistory(user, matchedId);
        FcmUtils.sendChatProposal(matchedFcmToken.getToken(), user.getNickname());
    }

    public List<ChatProposedResponse> getProposedList(Long userId) {
        return chatHistoryRepository.findByProposedId(userId)
                .stream().map(chatHistory -> {
                    User proposer = userCRUDService.findById(chatHistory.getProposerId());
                    return ChatProposedResponse.builder()
                            .proposerNickname(proposer.getNickname())
                            .isAccepted(chatHistory.getIsAccepted())
                            .build();
                })
                .toList();
    }

    public List<ChatProposalResponse> getProposalList(Long userId) {
        return chatHistoryRepository.findByProposerId(userId)
                .stream().map(chatHistory -> {
                    User proposed = userCRUDService.findById(chatHistory.getProposedId());
                    return ChatProposalResponse.builder()
                            .proposedNickname(proposed.getNickname())
                            .isAccepted(chatHistory.getIsAccepted())
                            .build();
                })
                .toList();
    }

    private void recordChatHistory(User user, Long matchedId) {
        ChatHistory chatHistory = ChatHistory.builder()
                .proposerId(user.getId())
                .proposedId(matchedId)
                .build();
        chatHistoryRepository.save(chatHistory);
    }
}

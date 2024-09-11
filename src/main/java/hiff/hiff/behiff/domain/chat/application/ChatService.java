package hiff.hiff.behiff.domain.chat.application;

import hiff.hiff.behiff.domain.chat.domain.ChatHistory;
import hiff.hiff.behiff.domain.chat.infrastructure.ChatHistoryRepository;
import hiff.hiff.behiff.domain.chat.presentation.dto.res.ChatProposalResponse;
import hiff.hiff.behiff.domain.user.application.UserCRUDService;
import hiff.hiff.behiff.domain.user.application.UserProfileService;
import hiff.hiff.behiff.domain.user.application.UserService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.presentation.dto.res.MyInfoResponse;
import hiff.hiff.behiff.global.auth.domain.FcmToken;
import hiff.hiff.behiff.global.auth.infrastructure.FcmTokenRepository;
import hiff.hiff.behiff.global.common.fcm.FcmUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<ChatProposalResponse> getProposedList(Long userId) {
        return chatHistoryRepository.findByProposedId(userId)
                .stream().map(chatHistory -> {
                    User proposer = userCRUDService.findById(chatHistory.getProposerId());
                    return ChatProposalResponse.builder()
                            .proposerNickname(proposer.getNickname())
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

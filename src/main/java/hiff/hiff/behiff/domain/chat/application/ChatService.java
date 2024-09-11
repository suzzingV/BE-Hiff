package hiff.hiff.behiff.domain.chat.application;

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

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final FcmTokenRepository fcmTokenRepository;
    private final UserCRUDService userCRUDService;

    public void proposeChat(User user, Long matchedId) {
        FcmToken matchedFcmToken = fcmTokenRepository.findByUserId(matchedId);

        FcmUtils.sendChatProposal(matchedFcmToken.getToken(), user.getNickname());
    }
}

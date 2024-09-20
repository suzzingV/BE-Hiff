package hiff.hiff.behiff.global.common.fcm;

import com.google.firebase.messaging.*;
import hiff.hiff.behiff.global.auth.exception.AuthException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class FcmUtils {

    private static final String PUSH_TITLE = "Hiff";
    private static final String CHAT_PROPOSAL_BODY = "님이 대화를 신청했습니다.";
    private static final String MATCHING_ALARM_BODY = "매칭 상대를 찾았습니다.";

    private final FirebaseMessaging firebaseMessaging;

    public void sendChatProposal(String token, String nickname) {
        Notification notification = Notification.builder()
                .setTitle(PUSH_TITLE)
                .setBody(nickname + CHAT_PROPOSAL_BODY)
                .build();
        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            throw new AuthException(ErrorCode.FCM_SEND_ERROR);
        }
    }

    public void sendMatchingAlarm(String token) {
        Notification notification = Notification.builder()
                .setTitle(PUSH_TITLE)
                .setBody(MATCHING_ALARM_BODY)
                .build();
        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        try {
            String response = firebaseMessaging.send(message);
            log.info("성공: " + response);
            log.info("성공: " + token);
        } catch (FirebaseMessagingException e) {
            log.info("실패: " + token);
            log.info(e.getMessage());
            throw new AuthException(ErrorCode.FCM_SEND_ERROR);
        }
    }
}

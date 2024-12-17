package hiff.hiff.behiff.global.common.fcm;

import com.google.firebase.messaging.*;
import hiff.hiff.behiff.global.auth.exception.AuthException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class FcmUtils {

    private static final String PUSH_TITLE = "Hiff";
    private static final String CHAT_SENDING_BODY = "님이 대화를 신청했습니다.";
    private static final String CHAT_ACCEPTANCE_BODY = "님이 대화를 수했습니다.";
    private static final String LIKE_SENDING_BODY = "님이 호감을 보냈습니다.";
    public static final String MATCHING_ALARM_BODY = "새로운 매칭 상대를 찾았습니다.";

    private final FirebaseMessaging firebaseMessaging;

    public void sendChatAlarm(Long proposerId, String token, String nickname) {
        Notification notification = Notification.builder()
                .setTitle(PUSH_TITLE)
                .setBody(nickname + CHAT_SENDING_BODY)
                .build();
        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .putData("senderId", proposerId.toString())
                .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            throw new AuthException(ErrorCode.FCM_SEND_ERROR);
        }
    }

    public void sendLikeAlarm(Long senderId, String token, String nickname) {
        Notification notification = Notification.builder()
                .setTitle(PUSH_TITLE)
                .setBody(nickname + LIKE_SENDING_BODY)
                .build();
        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .putData("senderId", senderId.toString())
                .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            throw new AuthException(ErrorCode.FCM_SEND_ERROR);
        }
    }

    public void sendChatAcceptAlarm(Long senderId, String token, String nickname) {
        Notification notification = Notification.builder()
                .setTitle(PUSH_TITLE)
                .setBody(nickname + CHAT_ACCEPTANCE_BODY)
                .build();
        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .putData("senderId", senderId.toString())
                .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            throw new AuthException(ErrorCode.FCM_SEND_ERROR);
        }
    }

    public static void sendMatchingAlarm(String token, Long matchedId) {
        Notification notification = Notification.builder()
                .setTitle(PUSH_TITLE)
                .setBody(MATCHING_ALARM_BODY)
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .putData("matchedId", matchedId.toString())
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("성공: " + response);
            log.info("성공: " + token);
        } catch (FirebaseMessagingException e) {
            log.info("실패: " + token);
            log.info(e.getMessage());
        }
    }
}

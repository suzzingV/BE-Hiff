package hiff.hiff.behiff.global.common.sms;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static hiff.hiff.behiff.global.common.fcm.FcmUtils.MATCHING_ALARM_BODY;

@Component
// TODO: Util 리팩토링
public class SmsUtil {

    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecret;
    @Value("${coolsms.from-number}")
    private String fromNumber;

    private DefaultMessageService messageService;

    @PostConstruct
    private void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret,
            "https://api.coolsms.co.kr");
    }

    // 단일 메시지 발송 예제
    public SingleMessageSentResponse sendVerificationCode(String to, String verificationCode) {
        String text = generateVerificationText(verificationCode);
        Message coolsms = generateMessage(to, text);

        return messageService.sendOne(new SingleMessageSendingRequest(coolsms));
    }

    public SingleMessageSentResponse sendAcceptMessage(String to, String proposerNickname, String proposerPhoneNum) {
        String text = generateAcceptanceText(proposerNickname, proposerPhoneNum);
        Message coolsms = generateMessage(to, text);

        return messageService.sendOne(new SingleMessageSendingRequest(coolsms));
    }

    public SingleMessageSentResponse sendProposeMessage(String to, String proposerNickname) {
        String text = generateProposeText(proposerNickname);
        Message coolsms = generateMessage(to, text);

        return messageService.sendOne(new SingleMessageSendingRequest(coolsms));
    }

    public SingleMessageSentResponse sendMatchingMessage(String to) {
        String text = generateMatchingText();
        Message coolsms = generateMessage(to, text);

        return messageService.sendOne(new SingleMessageSendingRequest(coolsms));
    }

    @NotNull
    private Message generateMessage(String to, String text) {
        Message coolsms = new Message();
        coolsms.setFrom(fromNumber);
        coolsms.setTo(to);
        coolsms.setText(text);   // 문자메세지 내용 설정
        return coolsms;
    }

    @NotNull
    private String generateVerificationText(String verificationCode) {
        return "[Hiff] 인증번호는 " + verificationCode + "입니다.";
    }

    @NotNull
    private String generateAcceptanceText(String nickname, String phoneNum) {
        return "[Hiff] " + nickname + "님이 대화 신청을 수락하였습니다. " + nickname + "님의 연락처는 " + phoneNum + " 입니다.";
    }

    @NotNull
    private String generateMatchingText() {
        return "[Hiff] " + MATCHING_ALARM_BODY;
    }

    @NotNull
    private String generateProposeText(String proposerNickname) {
        return "[Hiff] " + proposerNickname + "님이 대화를 신청했습니다. 수락을 원하시면 이 문자가 온 번호로 '수락'이라고 답장해주세요!";
    }
}

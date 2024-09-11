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

@Component
@Slf4j
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
        return "[Hiff] 인증번호를 입력해주세요 [" + verificationCode + "]";
    }

    @NotNull
    private String generateAcceptanceText(String nickname, String phoneNum) {
        return "[Hiff] " + nickname + "님이 대화 신청을 수락하였습니다. " + nickname + "님의 연락처는 " + phoneNum + " 입니다.";
    }
}

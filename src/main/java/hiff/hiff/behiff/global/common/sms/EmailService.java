package hiff.hiff.behiff.global.common.sms;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private JavaMailSender emailSender;

    public void sendErrorEmail(String errorMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("sjlim1999@naver.com");
        message.setSubject("Batch Job Error Notification");
        message.setText("An error occurred during batch processing: \n" + errorMessage);
        emailSender.send(message);
    }
}

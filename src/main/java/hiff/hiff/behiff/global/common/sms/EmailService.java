package hiff.hiff.behiff.global.common.sms;

import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.global.auth.exception.AuthException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Configuration
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public void sendErrorEmail(String errorMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("sjlim1999@naver.com");
        message.setSubject("Batch Job Error Notification");
        message.setText("An error occurred during batch processing: \n" + errorMessage);
        emailSender.send(message);
    }

    public void sendSignUpEmail() {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo("teamhiff24@gmail.com");
            helper.setSubject("누가 새로 가입함!");

            String htmlContent = "<p>외모 평가 해라! <a href='https://hiff.o-r.kr/evaluation/users'>여기 클릭!</a></p>";
            helper.setText(htmlContent, true);

            emailSender.send(message);
        } catch (MessagingException e) {
            throw new UserException(ErrorCode.FAILED_TO_SEND_EMAIL);
        }
    }
}

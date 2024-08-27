package hiff.hiff.behiff.global.common.batch;

import hiff.hiff.behiff.global.common.email.EmailService;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

@Component
public class CustomSkipListener implements SkipListener<String, String> {

    private final EmailService emailService;

    public CustomSkipListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void onSkipInRead(Throwable t) {
        emailService.sendErrorEmail("Error during read phase: " + t.getMessage());
    }

    @Override
    public void onSkipInProcess(String item, Throwable t) {
        emailService.sendErrorEmail("Error processing item: " + item + " - " + t.getMessage());
    }

    @Override
    public void onSkipInWrite(String item, Throwable t) {
        emailService.sendErrorEmail("Error writing item: " + item + " - " + t.getMessage());
    }
}

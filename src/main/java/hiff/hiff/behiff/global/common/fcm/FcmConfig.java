package hiff.hiff.behiff.global.common.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import hiff.hiff.behiff.global.auth.exception.AuthException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
@Slf4j
public class FcmConfig {

    @PostConstruct
    private void init() {
        String fileResourceURL = "fcm-keyfile.json";
        ClassPathResource resource = new ClassPathResource(fileResourceURL);

        FirebaseOptions options = null;
        try {
            options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                    .build();
        } catch (IOException e) {
            throw new AuthException(ErrorCode.FCM_INIT_ERROR, e.getMessage());
        }

        FirebaseApp.initializeApp(options);
    }
}

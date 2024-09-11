package hiff.hiff.behiff.global.common.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import hiff.hiff.behiff.global.auth.exception.AuthException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class FcmConfig {

    // TODO: 키 추가
//    @PostConstruct
    private void init() {
        String fileResourceURL = "security/Server-Security/fcm/tht-push-fcm-firebase-adminsdk-secretkey.json";
        ClassPathResource resource = new ClassPathResource(fileResourceURL);

        FirebaseOptions options = null;
        try {
            options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                    .build();
        } catch (IOException e) {
            throw new AuthException(ErrorCode.FCM_INIT_ERROR);
        }

        FirebaseApp.initializeApp(options);
    }
}

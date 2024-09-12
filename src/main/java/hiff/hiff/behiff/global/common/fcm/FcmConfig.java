package hiff.hiff.behiff.global.common.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import hiff.hiff.behiff.global.auth.exception.AuthException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import org.springframework.util.ResourceUtils;

@Configuration
@Slf4j
public class FcmConfig {

    @Value("${fcm.credentials.location}")
    private String keyFileName;

    @PostConstruct
    private void init() {

        FirebaseOptions options = null;
        try {
            InputStream keyFile = ResourceUtils.getURL(keyFileName).openStream();
            options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(keyFile))
                    .build();
        } catch (IOException e) {
            throw new AuthException(ErrorCode.FCM_INIT_ERROR, e.getMessage());
        }

        FirebaseApp.initializeApp(options);
    }
}

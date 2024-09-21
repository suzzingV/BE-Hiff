package hiff.hiff.behiff.global.common.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import hiff.hiff.behiff.global.auth.exception.AuthException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
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

    @Bean
    FirebaseApp firebaseApp() throws IOException {
        InputStream keyFile = ResourceUtils.getURL(keyFileName).openStream();
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(
                        keyFile))
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        return FirebaseMessaging.getInstance(firebaseApp());
    }
}

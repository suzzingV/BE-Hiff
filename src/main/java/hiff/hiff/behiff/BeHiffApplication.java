package hiff.hiff.behiff;

import hiff.hiff.behiff.domain.user.application.service.UserHobbyService;
import hiff.hiff.behiff.domain.user.application.service.UserLifeStyleService;
import hiff.hiff.behiff.domain.user.application.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
public class BeHiffApplication {

    //캐싱
    private final UserHobbyService userHobbyService;
    private final UserLifeStyleService userLifeStyleService;
    private final UserProfileService userProfileService;

    public static void main(String[] args) {
        SpringApplication.run(BeHiffApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner() {
//        userHobbyService.cacheHobbySimilarity();
//        userLifeStyleService.cacheLifeStyleSimilarity();
//        userProfileService.cacheMbtiSimilarity();
//        return args -> {
//        };
//    }
}

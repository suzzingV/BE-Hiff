package hiff.hiff.behiff;

import hiff.hiff.behiff.domain.profile.application.service.UserProfileService;
import hiff.hiff.behiff.global.util.DateCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
@ServletComponentScan
public class BeHiffApplication {

    private final UserProfileService userProfileService;

    public static void main(String[] args) {
        SpringApplication.run(BeHiffApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
//        userHobbyService.cacheHobbySimilarity();
//        userLifeStyleService.cacheLifeStyleSimilarity();
//        userProfileService.cacheMbtiSimilarity();
        DateCalculator.updateTodayDate();
        return args -> {
        };
    }
}

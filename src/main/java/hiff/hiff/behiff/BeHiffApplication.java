package hiff.hiff.behiff;

import hiff.hiff.behiff.domain.user.application.UserHobbyService;
import hiff.hiff.behiff.domain.user.application.UserLifeStyleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//@RequiredArgsConstructor
public class BeHiffApplication {

    //캐싱
//    private final UserHobbyService userHobbyService;
//    private final UserLifeStyleService userLifeStyleService;

    public static void main(String[] args) {
        SpringApplication.run(BeHiffApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner() {
////        userHobbyService.cacheHobbySimilarity();
//        userLifeStyleService.cacheLifeStyleSimilarity();
//        return args -> {
//        };
//    }
}

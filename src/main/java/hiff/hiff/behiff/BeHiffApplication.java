package hiff.hiff.behiff;

import hiff.hiff.behiff.domain.user.domain.entity.UserHobby;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BeHiffApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeHiffApplication.class, args);
    }
}

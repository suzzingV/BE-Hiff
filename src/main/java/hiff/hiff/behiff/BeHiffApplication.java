package hiff.hiff.behiff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BeHiffApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeHiffApplication.class, args);
    }
}

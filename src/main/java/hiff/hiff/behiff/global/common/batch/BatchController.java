package hiff.hiff.behiff.global.common.batch;

import hiff.hiff.behiff.domain.matching.application.service.MatchingService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.UserHobby;
import hiff.hiff.behiff.domain.user.domain.entity.UserLifeStyle;
import hiff.hiff.behiff.domain.user.infrastructure.UserHobbyRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserLifeStyleRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.global.common.redis.RedisService;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/batch")
public class BatchController {

    private final Job getUserByGenderJob;
    private final JobLauncher jobLauncher;
    private final UserRepository userRepository;
    private final UserHobbyRepository userHobbyRepository;
    private final UserLifeStyleRepository userLifeStyleRepository;
    private final RedisService redisService;

    public BatchController(@Qualifier("getUserByGenderJob") Job getUserByGenderJob, JobLauncher jobLauncher, UserHobbyRepository userHobbyRepository, UserRepository userRepository, RedisService redisService, UserLifeStyleRepository userLifeStyleRepository) {
        this.getUserByGenderJob = getUserByGenderJob;
        this.jobLauncher = jobLauncher;
        this.userRepository = userRepository;
        this.userHobbyRepository = userHobbyRepository;
        this.redisService = redisService;
        this.userLifeStyleRepository = userLifeStyleRepository;
    }

    @GetMapping("/gender")
    public ResponseEntity<Void> getUserByGender()
        throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
//        List<User> all = userRepository.findAll();
//        for(User female : all) {
//            List<UserHobby> userHobbies = userHobbyRepository.findByUserId(female.getId());
//            List<UserLifeStyle> userLifeStyles = userLifeStyleRepository.findByUserId(female.getId());
//
//            StringBuilder tmp = new StringBuilder();
//            for (UserHobby userHobby : userHobbies) {
//                tmp.append(userHobby.getHobbyId()).append("/");
//            }
//            redisService.setStrValue("ushob_" + female.getId(), tmp.toString(), Duration.ofDays(2));
//
//            tmp = new StringBuilder();
//            for (UserLifeStyle userLifeStyle : userLifeStyles) {
//                tmp.append(userLifeStyle.getLifeStyleId()).append("/");
//            }
//            redisService.setStrValue("uslif_" + female.getId(), tmp.toString(), Duration.ofDays(2));
//        }

        JobParameters jobParameters = new JobParametersBuilder()
            .addDate("currentTime", new Date())
            .toJobParameters();
        jobLauncher.run(getUserByGenderJob, jobParameters);
        return ResponseEntity.ok().build();

    }
}

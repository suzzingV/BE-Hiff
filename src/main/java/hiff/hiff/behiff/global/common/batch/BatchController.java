package hiff.hiff.behiff.global.common.batch;

import java.util.Date;
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

    public BatchController(@Qualifier("getUserByGenderJob") Job getUserByGenderJob, JobLauncher jobLauncher) {
        this.getUserByGenderJob = getUserByGenderJob;
        this.jobLauncher = jobLauncher;
    }

    @GetMapping("/gender")
    public ResponseEntity<Void> getUserByGender()
        throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
            .addDate("currentTime", new Date())
            .toJobParameters();
        jobLauncher.run(getUserByGenderJob, jobParameters);
        return ResponseEntity.ok().build();
    }
}

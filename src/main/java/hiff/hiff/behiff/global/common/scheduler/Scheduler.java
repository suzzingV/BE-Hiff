package hiff.hiff.behiff.global.common.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final JobLauncher jobLauncher;
    private final Job dailyMatchingInitJob;

    @Scheduled(cron = "0 0 0 * * ?")
    public void initMatchingData() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("currentTime", new Date()) // 현재 시간을 JobParameter로 추가
                .toJobParameters();
        jobLauncher.run(dailyMatchingInitJob, jobParameters);
    }
}

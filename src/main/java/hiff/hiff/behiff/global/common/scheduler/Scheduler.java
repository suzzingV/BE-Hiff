package hiff.hiff.behiff.global.common.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Scheduler {

    private final JobLauncher jobLauncher;
    private final Job dailyMatchingInitJob;
    private final Job hiffMatchingByMaleJob;

    public Scheduler(@Qualifier("dailyMatchingInitJob") Job dailyMatchingInitJob, @Qualifier("hiffMatchingByMaleJob") Job hiffMatchingByMaleJob, JobLauncher jobLauncher) {
        this.hiffMatchingByMaleJob = hiffMatchingByMaleJob;
        this.jobLauncher = jobLauncher;
        this.dailyMatchingInitJob = dailyMatchingInitJob;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void initMatchingData() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("currentTime", new Date())
                .toJobParameters();
        jobLauncher.run(dailyMatchingInitJob, jobParameters);
    }

    @Scheduled(cron = "0 43 2 * * ?")
    public void freeHiffMatching() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
            .addDate("currentTime", new Date())
            .toJobParameters();
        jobLauncher.run(hiffMatchingByMaleJob, jobParameters);
    }
}

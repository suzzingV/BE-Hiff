package hiff.hiff.behiff.global.common.scheduler;

import hiff.hiff.behiff.domain.user.domain.entity.GenderCount;
import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.GenderCountRepository;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private final JobLauncher jobLauncher;
    private final Job dailyMatchingInitJob;
    private final Job hiffMatchingByMaleJob;
    private final Job hiffMatchingByFemaleJob;
    private final GenderCountRepository genderCountRepository;

    public Scheduler(@Qualifier("dailyMatchingInitJob") Job dailyMatchingInitJob,
        @Qualifier("hiffMatchingByMaleJob") Job hiffMatchingByMaleJob,
        @Qualifier("hiffMatchingByFemaleJob") Job hiffMatchingByFemaleJob, JobLauncher jobLauncher,
        GenderCountRepository genderCountRepository) {
        this.hiffMatchingByMaleJob = hiffMatchingByMaleJob;
        this.hiffMatchingByFemaleJob = hiffMatchingByFemaleJob;
        this.jobLauncher = jobLauncher;
        this.dailyMatchingInitJob = dailyMatchingInitJob;
        this.genderCountRepository = genderCountRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void initMatchingData()
        throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
            .addDate("currentTime", new Date())
            .toJobParameters();
        jobLauncher.run(dailyMatchingInitJob, jobParameters);
    }

    @Scheduled(cron = "0 43 2 * * ?")
    public void freeHiffMatching()
        throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
            .addDate("currentTime", new Date())
            .toJobParameters();
        runHiffMatchingJob(jobParameters);
    }

    public void runHiffMatchingJob(JobParameters jobParameters)
        throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        int maleCount = getGenderCount(Gender.MALE);
        int femaleCount = getGenderCount(Gender.FEMALE);
        if (maleCount > femaleCount) {
            jobLauncher.run(hiffMatchingByMaleJob, jobParameters);
        } else {
            jobLauncher.run(hiffMatchingByFemaleJob, jobParameters);
        }
    }

    private int getGenderCount(Gender gender) {
        GenderCount genderCount = genderCountRepository.findById(gender)
            .orElseThrow(() -> new UserException(ErrorCode.GENDER_COUNT_NOT_FOUND));
        return genderCount.getCount();
    }
}

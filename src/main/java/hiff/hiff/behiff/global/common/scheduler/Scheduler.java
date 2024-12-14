package hiff.hiff.behiff.global.common.scheduler;

import java.util.Date;

import hiff.hiff.behiff.global.util.DateCalculator;
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
    private final Job randomMatcingJob;
//    private final GenderCountRepository genderCountRepository;

    public Scheduler(@Qualifier("dailyMatchingInitJob") Job dailyMatchingInitJob,
        @Qualifier("randomMatchingJob") Job randomMatchingJob, JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
        this.dailyMatchingInitJob = dailyMatchingInitJob;
//        this.genderCountRepository = genderCountRepository;
        this.randomMatcingJob = randomMatchingJob;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void initMatchingData()
        throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        DateCalculator.updateTodayDate();
        JobParameters jobParameters = new JobParametersBuilder()
            .addDate("currentTime", new Date())
            .toJobParameters();
        jobLauncher.run(dailyMatchingInitJob, jobParameters);
    }

    @Scheduled(cron = "0 0 */3 * * ?")
    public void randomMatching()
        throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
            .addDate("currentTime", new Date())
            .toJobParameters();
        runRandomMatchingJob(jobParameters);
    }

    public void runRandomMatchingJob(JobParameters jobParameters)
        throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
//        int maleCount = getGenderCount(Gender.MALE);
//        int femaleCount = getGenderCount(Gender.FEMALE);
//        if (maleCount > femaleCount) {
//            jobLauncher.run(hiffMatchingByMaleJob, jobParameters);
//        } else {
//            jobLauncher.run(hiffMatchingByFemaleJob, jobParameters);
//        }

        jobLauncher.run(randomMatcingJob, jobParameters);
    }

//    private int getGenderCount(Gender gender) {
//        GenderCount genderCount = genderCountRepository.findById(gender)
//            .orElseThrow(() -> new UserException(ErrorCode.GENDER_COUNT_NOT_FOUND));
//        return genderCount.getCount();
//    }
}

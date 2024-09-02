package hiff.hiff.behiff.global.common.batch.matching_init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DefaultJobExecutionListener implements JobExecutionListener {

    private long startTime;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis();
        log.info("Before Job: " + jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            long duration = System.currentTimeMillis() - startTime;
            log.info("Job Completed Successfully: " + jobExecution.getJobInstance().getJobName());
            log.info("Job took " + duration + " ms to complete.");
        } else {
            log.error("Job Failed: " + jobExecution.getJobInstance().getJobName());
        }
    }
}

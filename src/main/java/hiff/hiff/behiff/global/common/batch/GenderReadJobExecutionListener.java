package hiff.hiff.behiff.global.common.batch;

import static hiff.hiff.behiff.global.common.batch.BatchConfig.femaleList;
import static hiff.hiff.behiff.global.common.batch.BatchConfig.females;

import hiff.hiff.behiff.domain.matching.application.service.MatchingService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenderReadJobExecutionListener implements JobExecutionListener {

    private long startTime;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Job Completed Successfully: " + jobExecution.getJobInstance()
                .getJobName());
            long duration = System.currentTimeMillis() - startTime;
            log.info("Job took " + duration + " ms to complete.");
            females.clear();
            femaleList.clear();
        } else {
            log.error("Job Failed: " + jobExecution.getJobInstance().getJobName());
        }
    }
}

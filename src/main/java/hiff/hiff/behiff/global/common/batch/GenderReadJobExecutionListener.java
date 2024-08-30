package hiff.hiff.behiff.global.common.batch;

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

    private List<User> females = new ArrayList<>();
    private List<User> males = new ArrayList<>();

    private final MatchingService matchingService;
    private long startTime;

    public void addMales(List<? extends User> users) {
        males.addAll(users);
    }

    public void addFemales(List<? extends User> users) {
        females.addAll(users);
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            long duration = System.currentTimeMillis() - startTime;
            matchingService.getNewHiffMatching(males, females);
            males = new ArrayList<>();
            females = new ArrayList<>();
            log.info("Job Completed Successfully: " + jobExecution.getJobInstance()
                .getJobName());
            log.info("Job took " + duration + " ms to complete.");
        } else {
            log.error("Job Failed: " + jobExecution.getJobInstance().getJobName());
        }
    }
}

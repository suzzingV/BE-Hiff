package hiff.hiff.behiff.global.common.batch.hiff_matching;

import static hiff.hiff.behiff.global.common.batch.hiff_matching.HiffMatchingBatchConfig.matchedList;
import static hiff.hiff.behiff.global.common.batch.hiff_matching.HiffMatchingBatchConfig.matchedQueue;

import hiff.hiff.behiff.domain.matching.application.dto.UserWithMatchCount;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchedStepExecutionListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Step {} is about to start.", stepExecution.getStepName());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Step {} has completed with status: {}.",
            stepExecution.getStepName(),
            stepExecution.getStatus());
        Collections.shuffle(matchedList);
        for (User matched : matchedList) {
            matchedQueue.add(new UserWithMatchCount(matched, 0));
        }
        return stepExecution.getExitStatus();
    }
}

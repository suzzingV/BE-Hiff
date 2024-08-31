package hiff.hiff.behiff.global.common.batch;

import static hiff.hiff.behiff.global.common.batch.BatchConfig.femaleList;
import static hiff.hiff.behiff.global.common.batch.BatchConfig.females;

import hiff.hiff.behiff.domain.matching.application.service.UserWithMatchCount;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.UserHobby;
import hiff.hiff.behiff.domain.user.infrastructure.UserHobbyRepository;
import hiff.hiff.behiff.global.common.redis.RedisService;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FemaleStepExecutionListener implements StepExecutionListener {

    private final RedisService redisService;
    private final UserHobbyRepository userHobbyRepository;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Step {} is about to start.", stepExecution.getStepName());
        // 스텝 실행 전 필요한 로직을 여기에 추가
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Step {} has completed with status: {}.",
            stepExecution.getStepName(),
            stepExecution.getStatus());
        // 스텝 실행 후 필요한 로직을 여기에 추가
        Collections.shuffle(femaleList);
        for(User female : femaleList) {
            females.add(new UserWithMatchCount(female, 0));
        }
        return stepExecution.getExitStatus(); // 기본적으로 현재 스텝의 종료 상태를 반환
    }
}

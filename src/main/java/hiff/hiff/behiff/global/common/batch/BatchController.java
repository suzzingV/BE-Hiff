package hiff.hiff.behiff.global.common.batch;

import hiff.hiff.behiff.global.common.scheduler.Scheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/batch")
@RequiredArgsConstructor
public class BatchController {

    private final Scheduler scheduler;

    @GetMapping("/gender")
    public ResponseEntity<Void> getUserByGender()
        throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        scheduler.randomMatching();
        return ResponseEntity.ok().build();

    }

    @GetMapping("/matching/random")
    public ResponseEntity<Void> performRandomMatching()
            throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        scheduler.randomMatching();
        return ResponseEntity.ok().build();

    }

    @GetMapping("/init")
    public ResponseEntity<Void> initMatching()
            throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        scheduler.initMatchingData();
        return ResponseEntity.ok().build();

    }
}

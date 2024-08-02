package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.user.domain.entity.Job;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.JobRepository;
import hiff.hiff.behiff.domain.user.presentation.dto.req.JobRequest;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserJobService {

    private final JobRepository jobRepository;

    public void updateOriginJob(User user, Long jobId) {
            Job job = findById(jobId);
            user.changeJob(job.getName());
    }

    public void updateNewJob(User user, String jobName) {
        jobRepository.findByName(jobName)
                .ifPresentOrElse(Job::addCount, () -> {
                    Job job = Job.builder()
                            .name(jobName)
                            .build();
                    jobRepository.save(job);
                });
        user.changeJob(jobName);
    }

    private Job findById(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new UserException(ErrorCode.JOB_NOT_FOUND));
    }
}

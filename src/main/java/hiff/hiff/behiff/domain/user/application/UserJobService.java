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

    // TODO: 직종 직접 입력 추가
    public void updateJob(User user, JobRequest request) {
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new UserException(ErrorCode.JOB_NOT_FOUND));
        user.changeJob(job.getName());
        isJobExist(request.getJobId());
    }

    private void isJobExist(Long jobId) {
        jobRepository.findById(jobId)
                .orElseThrow(() -> new UserException(ErrorCode.JOB_NOT_FOUND));
    }
}

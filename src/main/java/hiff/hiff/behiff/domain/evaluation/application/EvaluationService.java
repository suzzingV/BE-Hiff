package hiff.hiff.behiff.domain.evaluation.application;

import hiff.hiff.behiff.domain.evaluation.domain.entity.Evaluation;
import hiff.hiff.behiff.domain.evaluation.exception.EvaluationException;
import hiff.hiff.behiff.domain.evaluation.infrastructure.EvaluationRepository;
import hiff.hiff.behiff.domain.evaluation.presentation.dto.res.EvaluationResponse;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;


    public EvaluationResponse getEvaluation(Long userId) {
        User evaluated = userRepository.findRandomByEvaluation(userId)
                .orElseThrow(() -> new EvaluationException(ErrorCode.USER_NOT_FOUND));
        return EvaluationResponse.builder()
                .evaluatedId(evaluated.getId())
                .photo(evaluated.getMainPhoto())
                .build();
    }
}

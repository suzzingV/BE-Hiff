package hiff.hiff.behiff.domain.evaluation.application;

import hiff.hiff.behiff.domain.evaluation.domain.entity.Evaluation;
import hiff.hiff.behiff.domain.evaluation.exception.EvaluationException;
import hiff.hiff.behiff.domain.evaluation.infrastructure.EvaluationRepository;
import hiff.hiff.behiff.domain.evaluation.presentation.dto.req.EvaluationRequest;
import hiff.hiff.behiff.domain.evaluation.presentation.dto.res.EvaluatedResponse;
import hiff.hiff.behiff.domain.evaluation.presentation.dto.res.EvaluationResponse;
import hiff.hiff.behiff.domain.user.application.UserCRUDService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final UserCRUDService userCRUDService;
    private final RedisService redisService;

    public EvaluatedResponse getEvaluated(Long evaluatorId) {
        User evaluator = userCRUDService.findUserById(evaluatorId);
        User evaluated = userCRUDService.findRandomByEvaluation(evaluatorId, evaluator.getGender());
        checkEvaluationAvailable(evaluatorId, evaluated.getId());

        return EvaluatedResponse.builder()
                .evaluatedId(evaluated.getId())
                .photo(evaluated.getMainPhoto())
                .build();
    }

    public EvaluationResponse evaluate(Long evaluatorId, EvaluationRequest request) {
        Long evaluatedId = request.getEvaluatedId();
        Integer score = request.getScore();

        checkEvaluationAvailable(evaluatorId, evaluatedId);
        updateEvaluatedScore(evaluatedId, score);
        createAndCountEvaluation(evaluatorId, evaluatedId, score);
        handleFreeCount(request.getIsPaid(), evaluatorId);

        return EvaluationResponse.builder()
                .evaluatedId(evaluatedId)
                .build();
    }

    private void createAndCountEvaluation(Long evaluatorId, Long evaluatedId, Integer score) {
        Evaluation evaluation = Evaluation.builder()
                .evaluatedId(evaluatedId)
                .evaluatorId(evaluatorId)
                .score(score)
                .build();
        evaluationRepository.save(evaluation);
    }

    private void handleFreeCount(boolean isPaid, Long evaluatorId) {
        if(!isPaid) {
            redisService.updateEvaluationValues(String.valueOf(evaluatorId));
        }
    }

    private void updateEvaluatedScore(Long evaluatedId, Integer score) {
        User evaluated = userCRUDService.findUserById(evaluatedId);
        evaluated.updateEvaluatedScore(score);
    }

    private void checkEvaluationAvailable(Long evaluatorId, Long evaluatedId) {
        User evaluator = userCRUDService.findUserById(evaluatorId);
        User evaluated = userCRUDService.findUserById(evaluatedId);

        if (!redisService.isEvaluationAvailable(String.valueOf(evaluatorId))) {
            throw new EvaluationException(ErrorCode.EVALUATION_COUNT_EXCEED);
        }

        if (evaluated.getGender() == evaluator.getGender()) {
            throw new EvaluationException(ErrorCode.EVALUATION_INVALID_GENDER);
        }

        evaluationRepository.findByEvaluatedIdAndEvaluatorId(evaluatedId, evaluatorId)
                .ifPresent(evaluation -> {
                    throw new EvaluationException(ErrorCode.EVALUATION_ALREADY);
                });
    }
}

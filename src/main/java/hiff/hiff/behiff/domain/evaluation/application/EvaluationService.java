package hiff.hiff.behiff.domain.evaluation.application;

import static hiff.hiff.behiff.global.common.redis.RedisService.EVALUATION_PREFIX;

import hiff.hiff.behiff.domain.evaluation.domain.entity.EvaluatedUser;
import hiff.hiff.behiff.domain.evaluation.domain.entity.Evaluation;
import hiff.hiff.behiff.domain.evaluation.exception.EvaluationException;
import hiff.hiff.behiff.domain.evaluation.infrastructure.EvaluatedUserRepository;
import hiff.hiff.behiff.domain.evaluation.infrastructure.EvaluationRepository;
import hiff.hiff.behiff.domain.evaluation.presentation.dto.req.EvaluationRequest;
import hiff.hiff.behiff.domain.evaluation.presentation.dto.res.EvaluatedResponse;
import hiff.hiff.behiff.domain.evaluation.presentation.dto.res.EvaluationResponse;
import hiff.hiff.behiff.domain.user.application.UserCRUDService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final EvaluatedUserRepository evaluatedUserRepository;
    private final UserCRUDService userCRUDService;
    private final RedisService redisService;

    public EvaluatedResponse getEvaluated(User evaluator) {
        checkEvaluationCount(evaluator.getId());
        EvaluatedUser evaluatedUser = evaluatedUserRepository.findByRandom(evaluator.getId(),
                evaluator.getGender())
            .orElseThrow(() -> new EvaluationException(ErrorCode.EVALUATION_NOT_FOUND));
        User evaluated = userCRUDService.findUserById(evaluatedUser.getUserId());

        return EvaluatedResponse.builder()
            .evaluatedId(evaluated.getId())
            .photo(evaluated.getMainPhoto())
            .build();
    }

    public EvaluationResponse evaluate(Long evaluatorId, EvaluationRequest request) {
        User evaluator = userCRUDService.findUserById(evaluatorId);
        User evaluated = userCRUDService.findUserById(request.getEvaluatedId());
        Integer score = request.getScore();

        checkEvaluationAvailable(evaluator, evaluated);
        updateEvaluatedScore(evaluated, score);
        createEvaluation(evaluator, evaluated, score);
        boolean isHeartProvided = countEvaluation(evaluator);

        return EvaluationResponse.builder()
            .evaluatedId(evaluated.getId())
            .isHeartProvided(isHeartProvided)
            .build();
    }

    public void addEvaluatedUser(Long userId, Gender gender) {
        EvaluatedUser evaluatedUser = EvaluatedUser.builder()
            .userId(userId)
            .gender(gender)
            .build();
        evaluatedUserRepository.save(evaluatedUser);
    }

    private void createEvaluation(User evaluator, User evaluated, Integer score) {
        Evaluation evaluation = Evaluation.builder()
            .evaluatedId(evaluated.getId())
            .evaluatorId(evaluator.getId())
            .score(score)
            .build();
        evaluationRepository.save(evaluation);
    }

    private boolean countEvaluation(User evaluator) {
        String key = EVALUATION_PREFIX + evaluator.getId();
        if (redisService.getIntValue(key) == 4) {
            redisService.updateIntValue(key);
            evaluator.addHeart(1);
            return true;
        }
        redisService.updateIntValue(key);
        return false;
    }

    private void updateEvaluatedScore(User evaluated, Integer score) {
        if (evaluated.getEvaluatedCount() == 9) {
            List<EvaluatedUser> evaluatedUsers = evaluatedUserRepository.findByUserId(
                evaluated.getId());
            Long evaluatedUserId = evaluatedUsers.get(0).getId();
            evaluatedUserRepository.deleteById(evaluatedUserId);
        }
        evaluated.updateEvaluatedScore(score);
    }

    private void checkEvaluationAvailable(User evaluator, User evaluated) {
        checkEvaluationCount(evaluator.getId());

        if (evaluated.getGender() == evaluator.getGender()) {
            throw new EvaluationException(ErrorCode.EVALUATION_INVALID_GENDER);
        }

        evaluationRepository.findByEvaluatedIdAndEvaluatorId(evaluated.getId(), evaluator.getId())
            .ifPresent(evaluation -> {
                throw new EvaluationException(ErrorCode.EVALUATION_ALREADY);
            });
    }

    private void checkEvaluationCount(Long evaluatorId) {
        String key = EVALUATION_PREFIX + evaluatorId;
        if (redisService.getIntValue(key) >= 5) {
            throw new EvaluationException(ErrorCode.EVALUATION_COUNT_EXCEED);
        }
    }
}

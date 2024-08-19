package hiff.hiff.behiff.domain.evaluation.infrastructure;

import hiff.hiff.behiff.domain.evaluation.domain.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    Optional<Evaluation> findByEvaluatedIdAndEvaluatorId(Long evaluatedId, Long evaluatorId);
}

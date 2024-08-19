package hiff.hiff.behiff.domain.evaluation.infrastructure;

import hiff.hiff.behiff.domain.evaluation.domain.entity.Evaluation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    Optional<Evaluation> findByEvaluatedIdAndEvaluatorId(Long evaluatedId, Long evaluatorId);
}

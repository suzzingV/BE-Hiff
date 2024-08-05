package hiff.hiff.behiff.domain.evaluation.infrastructure;

import hiff.hiff.behiff.domain.evaluation.domain.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
}

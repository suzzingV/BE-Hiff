package hiff.hiff.behiff.domain.evaluation.infrastructure;

import hiff.hiff.behiff.domain.evaluation.domain.entity.EvaluatedUser;
import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EvaluatedUserRepository extends JpaRepository<EvaluatedUser, Long> {

    @Query("SELECT e FROM EvaluatedUser e WHERE e.userId = :userId")
    List<EvaluatedUser> findByUserId(Long userId);

    @Query("""
            SELECT u FROM EvaluatedUser u
            WHERE u.userId NOT IN (SELECT e.evaluatedId FROM Evaluation e
                                    WHERE e.evaluatedId = u.userId AND e.evaluatorId = :evaluatorId)
            AND u.userId != :evaluatorId
            AND u.gender != :gender
            ORDER BY RAND() LIMIT 1
            """)
    Optional<EvaluatedUser> findByRandom(Long evaluatorId, Gender gender);
}

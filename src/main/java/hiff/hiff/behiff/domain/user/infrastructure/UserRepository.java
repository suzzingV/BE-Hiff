package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.deletedAt IS NULL")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.nickname = :nickname AND u.deletedAt IS NULL")
    Optional<User> findByNickname(String nickname);

    @Query("SELECT u FROM User u WHERE u.id NOT IN (SELECT e.evaluatedId FROM Evaluation e WHERE e.evaluatedId = u.id AND e.evaluatorId = :userId) AND u.id != :userId ORDER BY RAND() LIMIT 1")
    Optional<User> findRandomByEvaluation(Long userId);
}
package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.deletedAt IS NULL")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.nickname = :nickname AND u.deletedAt IS NULL")
    Optional<User> findByNickname(String nickname);

    @Query("""
            SELECT u FROM User u
            WHERE u.id NOT IN (SELECT m.matchedId FROM Matching m
                                    WHERE m.matchedId = u.id AND m.matcherId = :matcherId)
            AND u.id != :matcherId
            AND u.gender != :gender
            ORDER BY RAND() LIMIT 5
            """)
    List<User> getFiveMatched(Long matcherId, Gender gender);
}
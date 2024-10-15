package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.nickname = :nickname")
    Optional<User> findByNickname(String nickname);

    @Query("""
        SELECT u FROM User u
        WHERE u.id NOT IN (SELECT m.matchedId FROM Matching m
                                WHERE m.matchedId = u.id AND m.matcherId = :matcherId)
        AND u.id != :matcherId
        AND u.gender != :gender
        ORDER BY RAND() LIMIT 5
        """)
    List<User> getDailyMatched(Long matcherId, Gender gender);

    @Query("""
        SELECT u FROM User u
        WHERE u.id NOT IN (SELECT m.matchedId FROM Matching m
                                WHERE m.matchedId = u.id AND m.matcherId = :matcherId)
        AND u.id != :matcherId
        AND u.gender != :gender
        """)
    List<User> getMatched(Long matcherId, Gender gender);

    Page<User> findByGender(Gender gender, Pageable pageable);

    @Query("""
        SELECT u FROM User u
        WHERE u.evaluatedScore = :score
""")
    List<User> findUsersWithoutAppearanceScore(Double score);

    @Query("""
        SELECT u FROM User u
        WHERE u.phoneNum = :phoneNum
""")
    Optional<User> findByPhoneNum(String phoneNum);
}
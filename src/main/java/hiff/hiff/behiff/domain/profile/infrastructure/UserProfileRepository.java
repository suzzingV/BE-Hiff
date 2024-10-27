package hiff.hiff.behiff.domain.profile.infrastructure;

import hiff.hiff.behiff.domain.profile.domain.entity.UserProfile;
import hiff.hiff.behiff.domain.profile.domain.enums.Gender;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @Query("SELECT u FROM UserProfile u WHERE u.nickname = :nickname")
    Optional<UserProfile> findByNickname(String nickname);

    @Query("""
        SELECT u FROM UserProfile u
        WHERE u.id NOT IN (SELECT m.matchedId FROM Matching m
                                WHERE m.matchedId = u.id AND m.matcherId = :matcherId)
        AND u.id != :matcherId
        AND u.gender != :gender
        ORDER BY RAND() LIMIT 5
        """)
    List<UserProfile> getDailyMatched(Long matcherId, Gender gender);

    @Query("""
        SELECT u FROM UserProfile u
        WHERE u.id NOT IN (SELECT m.matchedId FROM Matching m
                                WHERE m.matchedId = u.id AND m.matcherId = :matcherId)
        AND u.id != :matcherId
        AND u.gender != :gender
        """)
    List<UserProfile> getMatched(Long matcherId, Gender gender);

    Page<UserProfile> findByGender(Gender gender, Pageable pageable);

    @Query("""
                SELECT u FROM UserProfile u
                WHERE u.evaluatedScore = :score
        """)
    List<UserProfile> findUsersWithoutAppearanceScore(Double score);

    @Query("SELECT p FROM UserProfile p WHERE p.userId = :userId")
    Optional<UserProfile> findByUserId(Long userId);


    void deleteByUserId(Long userId);
}
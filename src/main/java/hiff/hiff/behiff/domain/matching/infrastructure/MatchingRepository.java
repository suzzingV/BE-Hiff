package hiff.hiff.behiff.domain.matching.infrastructure;

import hiff.hiff.behiff.domain.matching.domain.entity.Matching;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MatchingRepository extends JpaRepository<Matching, Long> {

    Optional<Matching> findByMatcherIdAndMatchedId(Long matcherId, Long matchedId);

    @Query("""
                SELECT m.id FROM Matching m
                WHERE (m.matchedId = :user1 AND m.matcherId = :user2)
                OR (m.matchedId = :user2 AND m.matcherId = :user1)
        """)
    List<Long> findByUsers(Long user1, Long user2);
}

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
                WHERE (m.matchedId = :matchedId AND m.matcherId = :matcherId)
        """)
    Optional<Matching> findByUsers(Long matcherId, Long matchedId);

    void deleteByMatchedIdOrMatcherId(Long matchedId, Long matcherId);
}

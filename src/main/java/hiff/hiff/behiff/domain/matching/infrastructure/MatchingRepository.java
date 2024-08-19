package hiff.hiff.behiff.domain.matching.infrastructure;

import hiff.hiff.behiff.domain.matching.domain.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchingRepository extends JpaRepository<Matching, Long> {

    Optional<Matching> findByMatcherIdAndMatchedId(Long matcherId, Long matchedId);
}

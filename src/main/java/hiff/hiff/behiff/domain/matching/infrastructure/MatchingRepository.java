package hiff.hiff.behiff.domain.matching.infrastructure;

import hiff.hiff.behiff.domain.matching.domain.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingRepository extends JpaRepository<Matching, Long> {

}

package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.Belief;
import hiff.hiff.behiff.domain.user.domain.entity.Hobby;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeliefRepository extends JpaRepository<Belief, Long> {
    Optional<Belief> findByName(String beliefName);
}

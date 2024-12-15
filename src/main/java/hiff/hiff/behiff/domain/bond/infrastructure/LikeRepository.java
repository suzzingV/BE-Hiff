package hiff.hiff.behiff.domain.bond.infrastructure;

import hiff.hiff.behiff.domain.bond.domain.Like;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    List<Like> findBySenderId(Long senderId);

    List<Like> findByResponderId(Long responderId);

    Optional<Like> findBySenderIdAndResponderId(Long senderId, Long responderId);
}

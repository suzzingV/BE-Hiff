package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.UserPos;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPosRepository extends JpaRepository<UserPos, Long> {

    Optional<UserPos> findByUserId(Long userId);
}

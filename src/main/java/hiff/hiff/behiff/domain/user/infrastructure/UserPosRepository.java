package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.UserPos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPosRepository extends JpaRepository<UserPos, Long> {

    Optional<UserPos> findByUserId(Long userId);
}

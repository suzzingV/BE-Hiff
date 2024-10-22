package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.UserGrad;
import hiff.hiff.behiff.domain.user.domain.entity.UserUniversity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGradRepository extends JpaRepository<UserGrad, Long> {

    Optional<UserGrad> findByUserId(Long userId);
}

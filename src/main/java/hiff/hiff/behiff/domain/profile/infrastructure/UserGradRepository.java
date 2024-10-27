package hiff.hiff.behiff.domain.profile.infrastructure;

import hiff.hiff.behiff.domain.profile.domain.entity.UserGrad;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGradRepository extends JpaRepository<UserGrad, Long> {

    Optional<UserGrad> findByUserId(Long userId);
}

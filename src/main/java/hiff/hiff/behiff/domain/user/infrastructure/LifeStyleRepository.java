package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.LifeStyle;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LifeStyleRepository extends JpaRepository<LifeStyle, Long> {
    Optional<LifeStyle> findByName(String lifeStyleName);
}

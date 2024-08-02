package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.UserLifeStyle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserLifeStyleRepository extends JpaRepository<UserLifeStyle, Long> {
    List<UserLifeStyle> findByUserId(Long userId);
}

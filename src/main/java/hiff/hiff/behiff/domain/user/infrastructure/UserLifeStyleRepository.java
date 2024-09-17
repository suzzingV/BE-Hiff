package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.UserLifeStyle;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLifeStyleRepository extends JpaRepository<UserLifeStyle, Long> {

    List<UserLifeStyle> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}

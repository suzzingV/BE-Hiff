package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.UserLifeStyle;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserLifeStyleRepository extends JpaRepository<UserLifeStyle, Long> {

    List<UserLifeStyle> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("""
                    DELETE FROM UserLifeStyle l WHERE l.userId = :userId
        """)
    void deleteByUserId(Long userId);
}

package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.UserFashion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserFashionRepository extends JpaRepository<UserFashion, Long> {

    List<UserFashion> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("""
                    DELETE FROM UserFashion f WHERE f.userId = :userId
        """)
    void deleteByUserId(Long userId);
}

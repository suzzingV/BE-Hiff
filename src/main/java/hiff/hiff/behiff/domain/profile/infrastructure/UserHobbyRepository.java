package hiff.hiff.behiff.domain.profile.infrastructure;

import hiff.hiff.behiff.domain.profile.domain.entity.UserHobby;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserHobbyRepository extends JpaRepository<UserHobby, Long> {

    Optional<UserHobby> findByUserIdAndHobbyId(Long userId, Long hobbyId);

    List<UserHobby> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("""
                    DELETE FROM UserHobby h WHERE h.userId = :userId
        """)
    void deleteByUserId(Long userId);
}

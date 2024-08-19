package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.UserHobby;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserHobbyRepository extends JpaRepository<UserHobby, Long> {

    Optional<UserHobby> findByUserIdAndHobbyId(Long userId, Long hobbyId);

    List<UserHobby> findByUserId(Long userId);
}

package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.UserBelief;
import hiff.hiff.behiff.domain.user.domain.entity.UserHobby;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBeliefRepository extends JpaRepository<UserBelief, Long> {
    List<UserBelief> findByUserId(Long userId);
}

package hiff.hiff.behiff.domain.plan.infrastructure;

import hiff.hiff.behiff.domain.plan.domain.entity.UserPlan;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPlanRepository extends JpaRepository<UserPlan, Long> {

    Optional<UserPlan> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}

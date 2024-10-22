package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.UserIncome;
import hiff.hiff.behiff.domain.user.domain.entity.UserUniversity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserIncomeRepository extends JpaRepository<UserIncome, Long> {

    Optional<UserIncome> findByUserId(Long userId);
}

package hiff.hiff.behiff.domain.profile.infrastructure;

import hiff.hiff.behiff.domain.profile.domain.entity.UserIncome;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserIncomeRepository extends JpaRepository<UserIncome, Long> {

    Optional<UserIncome> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}

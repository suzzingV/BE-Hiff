package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.UserCareer;
import hiff.hiff.behiff.domain.user.domain.entity.UserFashion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserCareerRepository extends JpaRepository<UserCareer, Long> {

    Optional<UserCareer> findByUserId(Long userId);
}

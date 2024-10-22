package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.UserCareer;
import hiff.hiff.behiff.domain.user.domain.entity.UserUniversity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserUniversityRepository extends JpaRepository<UserUniversity, Long> {

    Optional<UserUniversity> findByUserId(Long userId);
}

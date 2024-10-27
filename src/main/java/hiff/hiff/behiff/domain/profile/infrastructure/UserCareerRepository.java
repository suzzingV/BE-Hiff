package hiff.hiff.behiff.domain.profile.infrastructure;

import hiff.hiff.behiff.domain.profile.domain.entity.UserCareer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCareerRepository extends JpaRepository<UserCareer, Long> {

    Optional<UserCareer> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}

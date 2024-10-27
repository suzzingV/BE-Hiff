package hiff.hiff.behiff.domain.profile.infrastructure;

import hiff.hiff.behiff.domain.profile.domain.entity.UserUniversity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserUniversityRepository extends JpaRepository<UserUniversity, Long> {

    Optional<UserUniversity> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}

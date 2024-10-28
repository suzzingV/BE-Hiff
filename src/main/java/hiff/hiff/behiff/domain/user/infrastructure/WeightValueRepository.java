package hiff.hiff.behiff.domain.user.infrastructure;

import java.util.Optional;

import hiff.hiff.behiff.domain.weighting.domain.entity.UserWeighting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WeightValueRepository extends JpaRepository<UserWeighting, Long> {

    @Query("SELECT w FROM UserWeighting w WHERE w.userId = :userId")
    Optional<UserWeighting> findByUserId(@Param("userId") Long userId);

    void deleteByUserId(Long userId);
}
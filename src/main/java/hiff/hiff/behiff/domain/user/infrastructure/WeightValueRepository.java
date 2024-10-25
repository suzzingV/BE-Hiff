package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.weighting.domain.entity.Weighting;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WeightValueRepository extends JpaRepository<Weighting, Long> {

    @Query("SELECT w FROM Weighting w WHERE w.userId = :userId")
    Optional<Weighting> findByUserId(@Param("userId") Long userId);

    void deleteByUserId(Long userId);
}
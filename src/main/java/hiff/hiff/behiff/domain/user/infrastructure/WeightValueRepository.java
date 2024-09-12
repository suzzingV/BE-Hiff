package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.WeightValue;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WeightValueRepository extends JpaRepository<WeightValue, Long> {

    @Query("SELECT w FROM WeightValue w WHERE w.userId = :userId")
    Optional<WeightValue> findByUserId(@Param("userId") Long userId);
}
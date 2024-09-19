package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.compositeKey.LifeStyleSimId;
import hiff.hiff.behiff.domain.user.domain.entity.HobbySimilarity;
import hiff.hiff.behiff.domain.user.domain.entity.LifeStyleSimilarity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LifeStyleSimilarityRepository extends JpaRepository<LifeStyleSimilarity, LifeStyleSimId> {
}

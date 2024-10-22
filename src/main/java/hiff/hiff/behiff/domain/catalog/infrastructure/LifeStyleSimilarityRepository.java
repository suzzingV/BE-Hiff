package hiff.hiff.behiff.domain.catalog.infrastructure;

import hiff.hiff.behiff.domain.catalog.domain.compositeKey.LifeStyleSimId;
import hiff.hiff.behiff.domain.catalog.domain.entity.LifeStyleSimilarity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LifeStyleSimilarityRepository extends
    JpaRepository<LifeStyleSimilarity, LifeStyleSimId> {

}

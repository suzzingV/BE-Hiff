package hiff.hiff.behiff.domain.catalog.infrastructure;

import hiff.hiff.behiff.domain.catalog.domain.compositeKey.MbtiScoreKey;
import hiff.hiff.behiff.domain.catalog.domain.entity.MbtiScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MbtiScoreRepository extends JpaRepository<MbtiScore, MbtiScoreKey> {

}

package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.compositeKey.HobbySimId;
import hiff.hiff.behiff.domain.user.domain.entity.HobbySimilarity;
import hiff.hiff.behiff.domain.user.domain.entity.LifeStyle;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HobbySimilarityRepository extends JpaRepository<HobbySimilarity, HobbySimId> {

    @Query("select h from HobbySimilarity h WHERE h.id.fromHobbyId = :fromId")
    List<HobbySimilarity> findByFromId(Long fromId);
}

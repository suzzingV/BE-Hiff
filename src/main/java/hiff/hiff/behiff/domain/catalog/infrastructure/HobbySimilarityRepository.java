package hiff.hiff.behiff.domain.catalog.infrastructure;

import hiff.hiff.behiff.domain.catalog.domain.compositeKey.HobbySimId;
import hiff.hiff.behiff.domain.catalog.domain.entity.HobbySimilarity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HobbySimilarityRepository extends JpaRepository<HobbySimilarity, HobbySimId> {

    @Query("select h from HobbySimilarity h WHERE h.id.fromHobbyId = :fromId")
    List<HobbySimilarity> findByFromId(Long fromId);
}

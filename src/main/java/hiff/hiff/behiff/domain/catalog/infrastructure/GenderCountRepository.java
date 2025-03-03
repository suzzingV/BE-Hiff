package hiff.hiff.behiff.domain.catalog.infrastructure;

import hiff.hiff.behiff.domain.catalog.domain.entity.GenderCount;
import hiff.hiff.behiff.domain.profile.domain.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenderCountRepository extends JpaRepository<GenderCount, Gender> {

}

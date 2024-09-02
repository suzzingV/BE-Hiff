package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.GenderCount;
import hiff.hiff.behiff.domain.user.domain.entity.Hobby;
import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenderCountRepository extends JpaRepository<GenderCount, Gender> {

}

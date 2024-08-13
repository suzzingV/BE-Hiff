package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.LifeStyle;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface LifeStyleRepository extends JpaRepository<LifeStyle, Long> {

    Optional<LifeStyle> findByName(String lifeStyleName);

    @Query("select l from LifeStyle l order by l.count desc")
    @NonNull
    List<LifeStyle> findAll();
}

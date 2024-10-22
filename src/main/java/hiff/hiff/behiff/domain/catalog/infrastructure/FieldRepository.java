package hiff.hiff.behiff.domain.catalog.infrastructure;

import hiff.hiff.behiff.domain.catalog.domain.entity.Field;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface FieldRepository extends JpaRepository<Field, Long> {

    Optional<Field> findByName(String careerName);

    @Query("select c from Field c order by c.count desc")
    @NonNull
    List<Field> findAll();
}

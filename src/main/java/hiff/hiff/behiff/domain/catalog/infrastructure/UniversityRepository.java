package hiff.hiff.behiff.domain.catalog.infrastructure;

import hiff.hiff.behiff.domain.catalog.domain.entity.Field;
import hiff.hiff.behiff.domain.catalog.domain.entity.University;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface UniversityRepository extends JpaRepository<University, Long> {

    @Query("select c from University c")
    @NonNull
    List<University> findAll();
}

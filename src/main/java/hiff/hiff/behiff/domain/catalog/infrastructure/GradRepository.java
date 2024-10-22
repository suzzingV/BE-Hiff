package hiff.hiff.behiff.domain.catalog.infrastructure;

import hiff.hiff.behiff.domain.catalog.domain.entity.Grad;
import hiff.hiff.behiff.domain.catalog.domain.entity.University;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface GradRepository extends JpaRepository<Grad, Long> {

    @Query("select c from Grad c")
    @NonNull
    List<Grad> findAll();
}

package hiff.hiff.behiff.domain.catalog.infrastructure;

import hiff.hiff.behiff.domain.catalog.domain.entity.Hobby;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface HobbyRepository extends JpaRepository<Hobby, Long> {

    Optional<Hobby> findByName(String hobbyName);

    @Query("select h from Hobby h order by h.count desc")
    @NonNull
    List<Hobby> findAll();
}

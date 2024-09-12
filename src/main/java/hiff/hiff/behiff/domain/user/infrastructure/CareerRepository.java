package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.Career;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface CareerRepository extends JpaRepository<Career, Long> {

    Optional<Career> findByName(String careerName);

    @Query("select c from Career c order by c.count desc")
    @NonNull
    List<Career> findAll();
}

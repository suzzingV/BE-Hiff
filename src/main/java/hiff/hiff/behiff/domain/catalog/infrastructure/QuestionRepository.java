package hiff.hiff.behiff.domain.catalog.infrastructure;

import hiff.hiff.behiff.domain.catalog.domain.entity.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("select q from Question q")
    @NonNull
    List<Question> findAll();
}

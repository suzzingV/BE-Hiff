package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.Career;
import hiff.hiff.behiff.domain.user.domain.entity.Question;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("select q from Question q")
    @NonNull
    List<Question> findAll();
}

package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.UserIntroduction;
import hiff.hiff.behiff.domain.user.domain.entity.UserLifeStyle;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserIntroductionRepository extends JpaRepository<UserIntroduction, Long> {

    List<UserIntroduction> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("""
            DELETE FROM UserIntroduction i WHERE i.userId = :userId
""")
    void deleteByUserIdAndQuestionId(Long userId);

    Optional<UserIntroduction> findByUserIdAndQuestionId(Long userId, Long questionId);
}

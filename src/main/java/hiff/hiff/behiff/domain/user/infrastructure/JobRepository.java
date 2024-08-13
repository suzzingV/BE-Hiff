package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.Job;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface JobRepository extends JpaRepository<Job, Long> {

    Optional<Job> findByName(String jobName);

    @Query("select j from Job j order by j.count desc")
    @NonNull
    List<Job> findAll();
}

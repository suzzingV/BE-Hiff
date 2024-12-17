package hiff.hiff.behiff.domain.bond.infrastructure;

import hiff.hiff.behiff.domain.bond.domain.Chat;
import hiff.hiff.behiff.domain.bond.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findBySenderIdAndResponderId(Long senderId, Long responderId);

    @Query("""
        select c from Chat c
        where (c.senderId = :user1 and c.responderId = :user2)
        or (c.responderId = :user1 and c.senderId = :user2)
""")
    Optional<Chat> findByUsers(Long user1, Long user2);
}

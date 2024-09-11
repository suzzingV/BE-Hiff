package hiff.hiff.behiff.domain.chat.infrastructure;

import hiff.hiff.behiff.domain.chat.domain.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
}

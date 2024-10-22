package hiff.hiff.behiff.domain.chat.infrastructure;

import hiff.hiff.behiff.domain.chat.domain.ChatHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {

    List<ChatHistory> findByProposedId(Long proposedId);

    List<ChatHistory> findByProposerId(Long proposerId);

    Optional<ChatHistory> findByProposerIdAndProposedId(Long proposerId, Long proposedId);

    void deleteByProposedIdOrProposedId(Long proposedId, Long proposeId);
}

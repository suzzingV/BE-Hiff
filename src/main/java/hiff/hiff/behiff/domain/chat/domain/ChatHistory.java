package hiff.hiff.behiff.domain.chat.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_history_TB")
public class ChatHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long proposerId;

    @Column(nullable = false)
    private Long proposedId;

    @Builder
    private ChatHistory(Long proposerId, Long proposedId) {
        this.proposerId = proposerId;
        this.proposedId = proposedId;
    }
}

package hiff.hiff.behiff.domain.bond.domain;

import hiff.hiff.behiff.domain.matching.domain.enums.MatchingStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_TB")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long responderId;

    @Column(nullable = false)
    private MatchingStatus status;

    @Builder
    private Chat(Long senderId, Long responderId, MatchingStatus status) {
        this.senderId = senderId;
        this.responderId = responderId;
        this.status = status;
    }
}

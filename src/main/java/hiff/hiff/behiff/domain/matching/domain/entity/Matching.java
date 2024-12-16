package hiff.hiff.behiff.domain.matching.domain.entity;

import hiff.hiff.behiff.domain.matching.domain.enums.MatchingStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "matching_TB",
    indexes = {
        @Index(name = "idx_users_id", columnList = "matcherId, matchedId")
    })
@Getter
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long matcherId;

    @Column(nullable = false)
    private Long matchedId;

    @Enumerated(EnumType.STRING)
    private MatchingStatus status;

    @Builder
    private Matching(Long matcherId, Long matchedId) {
        this.matcherId = matcherId;
        this.matchedId = matchedId;
        this.status = MatchingStatus.INIT;
    }
}

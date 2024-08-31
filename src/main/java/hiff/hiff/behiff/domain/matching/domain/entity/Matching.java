package hiff.hiff.behiff.domain.matching.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
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

    @Builder
    private Matching(Long matcherId, Long matchedId) {
        this.matcherId = matcherId;
        this.matchedId = matchedId;
    }
}

package hiff.hiff.behiff.domain.matching.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "matching_TB")
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

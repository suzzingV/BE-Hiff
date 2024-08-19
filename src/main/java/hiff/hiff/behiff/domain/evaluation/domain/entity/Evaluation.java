package hiff.hiff.behiff.domain.evaluation.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "evaluation_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long evaluatorId;

    private Long evaluatedId;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer score;

    @Builder
    private Evaluation(Long evaluatedId, Long evaluatorId, Integer score) {
        this.evaluatedId = evaluatedId;
        this.evaluatorId = evaluatorId;
        this.score = score;
    }
}

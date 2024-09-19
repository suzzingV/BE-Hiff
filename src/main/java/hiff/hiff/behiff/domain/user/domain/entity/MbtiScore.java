package hiff.hiff.behiff.domain.user.domain.entity;

import hiff.hiff.behiff.domain.user.domain.compositeKey.MbtiScoreKey;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "mbti_score_TB")
public class MbtiScore {

    @EmbeddedId
    private MbtiScoreKey id;

    private int score;

    @Builder
    private MbtiScore(MbtiScoreKey id, int score) {
        this.id = id;
        this.score = score;
    }
}

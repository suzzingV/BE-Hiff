package hiff.hiff.behiff.domain.user.domain.entity;

import hiff.hiff.behiff.domain.user.domain.compositeKey.HobbySimId;
import hiff.hiff.behiff.domain.user.domain.compositeKey.LifeStyleSimId;
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
@Table(name = "life_style_similarity_TB")
public class LifeStyleSimilarity {

    @EmbeddedId
    private LifeStyleSimId id;

    private Double similarity;

    @Builder
    private LifeStyleSimilarity(LifeStyleSimId id, Double similarity) {
        this.id = id;
        this.similarity = similarity;
    }
}

package hiff.hiff.behiff.domain.user.domain.entity;

import hiff.hiff.behiff.domain.user.domain.compositeKey.HobbySimId;
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
@Table(name = "hobby_similarity_TB")
public class HobbySimilarity {

    @EmbeddedId
    private HobbySimId id;

    private Double similarity;

    @Builder
    private HobbySimilarity(HobbySimId id, Double similarity) {
        this.id = id;
        this.similarity = similarity;
    }
}

package hiff.hiff.behiff.domain.weighting.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_weighting_TB",
    indexes = {
        @Index(name = "idx_w_user_id", columnList = "userId")
    })
@Getter
public class UserWeighting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private Integer appearance;

    private Integer hobby;

    private Integer lifeStyle;

    private Integer mbti;

    private Integer total;

    @Builder
    private UserWeighting(Long userId) {
        this.userId = userId;
    }

    public void changeWeightValue(Integer appearance, Integer hobby,
        Integer lifeStyle, Integer mbti) {
//        this.income = income;
        this.appearance = appearance;
        this.hobby = hobby;
        this.lifeStyle = lifeStyle;
        this.mbti = mbti;
        this.total = hobby + lifeStyle + mbti + appearance;
    }
}

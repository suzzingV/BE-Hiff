package hiff.hiff.behiff.domain.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "weight_value_TB")
@Getter
public class WeightValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer income;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer appearance;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer hobby;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer lifeStyle;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer mbti;

    @Column(nullable = false)
    private Integer total;

    @Builder
    private WeightValue(Long userId) {
        this.userId = userId;
        this.income = 3;
        this.appearance = 3;
        this.hobby = 3;
        this.lifeStyle = 3;
        this.mbti = 3;
        this.total = 3 * 5;
    }

    public void changeWeightValue(Integer income, Integer appearance, Integer hobby,
        Integer lifeStyle, Integer mbti) {
        this.income = income;
        this.appearance = appearance;
        this.hobby = hobby;
        this.lifeStyle = lifeStyle;
        this.mbti = mbti;
        this.total = income + appearance + hobby + lifeStyle + mbti;
    }
}

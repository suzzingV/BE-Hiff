package hiff.hiff.behiff.domain.evaluation.domain.entity;

import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "evaluated_user")
public class EvaluatedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Builder
    private EvaluatedUser(Long userId, Gender gender) {
        this.userId = userId;
        this.gender = gender;
    }

    public void changeGender(Gender gender) {
        this.gender = gender;
    }
}

package hiff.hiff.behiff.domain.user.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_life_sytle_TB")
public class UserLifeStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long lifeStyleId;

    @Builder
    private UserLifeStyle(Long userId, Long lifeStyleId) {
        this.userId = userId;
        this.lifeStyleId = lifeStyleId;
    }
}

package hiff.hiff.behiff.domain.user.domain.entity;

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
@Getter
@Table(name = "user_life_style_TB",
    indexes = {
        @Index(name = "idx_ls_user_id", columnList = "userId")
    })
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

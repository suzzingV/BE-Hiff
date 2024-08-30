package hiff.hiff.behiff.domain.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_pos_TB")
@Entity
public class UserPos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double x;

    @Column(nullable = false)
    private Double y;

    private Long userId;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    private UserPos(Double x, Double y, Long userId) {
        this.x = x;
        this.y = y;
        this.userId = userId;
    }

    public void changePos(Double x, Double y) {
        this.x = x;
        this.y = y;
        this.updatedAt = LocalDateTime.now();
    }
}

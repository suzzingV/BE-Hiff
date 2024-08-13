package hiff.hiff.behiff.domain.user.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_pos_TB")
@Entity
public class UserPos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String x;

    @Column(nullable = false, length = 20)
    private String y;

    private Long userId;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    private UserPos(String x, String y, Long userId) {
        this.x = x;
        this.y = y;
        this.userId = userId;
    }

    public void changePos(String x, String y) {
        this.x = x;
        this.y = y;
        this.updatedAt = LocalDateTime.now();
    }
}

package hiff.hiff.behiff.domain.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_pos_TB",
    indexes = {
        @Index(name = "idx_pos_user_id", columnList = "userId")
    })
@Entity
public class UserPos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lon;

    private Long userId;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    private UserPos(Double lat, Double lon, Long userId) {
        this.lat = lat;
        this.lon = lon;
        this.userId = userId;
    }

    public void changePos(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
        this.updatedAt = LocalDateTime.now();
    }
}

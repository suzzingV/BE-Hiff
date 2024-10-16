package hiff.hiff.behiff.domain.user.domain.entity;

import hiff.hiff.behiff.domain.user.domain.enums.Fashion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "user_fashion_TB",
    indexes = {
        @Index(name = "idx_fs_user_id", columnList = "userId")
    })
public class UserFashion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Fashion fashion;

    @Builder
    private UserFashion(Long userId, Fashion fashion) {
        this.userId = userId;
        this.fashion = fashion;
    }
}

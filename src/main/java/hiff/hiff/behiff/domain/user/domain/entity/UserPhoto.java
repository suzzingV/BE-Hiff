package hiff.hiff.behiff.domain.user.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_photo_TB")
public class UserPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 2048)
    private String photoUrl;

    @Builder
    private UserPhoto(Long userId, String photoUrl) {
        this.userId = userId;
        this.photoUrl = photoUrl;
    }
}

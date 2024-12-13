package hiff.hiff.behiff.domain.profile.domain.entity;

import hiff.hiff.behiff.domain.profile.domain.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "verification_photo_TB")
public class VerificationPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 2048)
    private String photoUrl;

    @Enumerated(EnumType.STRING)
    private VerificationStatus status;

    @Builder
    private VerificationPhoto(Long userId, String photoUrl, VerificationStatus status) {
        this.userId = userId;
        this.photoUrl = photoUrl;
        this.status = status;
    }

    public void updatePhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void updateVerificationStatus(VerificationStatus status) {
        this.status = status;
    }
}

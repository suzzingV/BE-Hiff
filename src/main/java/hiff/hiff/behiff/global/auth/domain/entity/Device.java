package hiff.hiff.behiff.global.auth.domain.entity;

import hiff.hiff.behiff.global.auth.domain.enums.OS;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "device_TB")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String fcmToken;

    @Enumerated(EnumType.STRING)
    private OS os;

    @Builder
    private Device(Long userId, String fcmToken, OS os) {
        this.userId = userId;
        this.fcmToken = fcmToken;
        this.os = os;
    }

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void updateOs(OS os) {
        this.os = os;
    }
}

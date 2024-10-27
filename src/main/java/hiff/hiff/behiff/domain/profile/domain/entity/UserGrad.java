package hiff.hiff.behiff.domain.profile.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_grad_TB")
public class UserGrad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String name;

    private String verification;

    private Boolean isVerified;

    @Builder
    private UserGrad(Long userId, String name, String verification) {
        this.userId = userId;
        this.name = name;
        this.verification = verification;
        this.isVerified = false;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeVerification(String verification) {
        this.verification = verification;
    }
}

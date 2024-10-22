package hiff.hiff.behiff.domain.user.domain.entity;

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
@Table(name = "user_income_TB")
public class UserIncome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Integer income;

    private String verification;

    private Boolean isVerified;

    @Builder
    private UserIncome(Long userId, Integer income, String verification) {
        this.userId = userId;
        this.income = income;
        this.verification = verification;
        this.isVerified = false;
    }

    public void changeVerification(String verification) {
        this.verification = verification;
    }
}

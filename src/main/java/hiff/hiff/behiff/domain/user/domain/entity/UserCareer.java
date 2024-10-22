package hiff.hiff.behiff.domain.user.domain.entity;

import hiff.hiff.behiff.domain.user.domain.enums.Company;
import hiff.hiff.behiff.domain.user.domain.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "user_career_TB")
public class UserCareer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String field;

    @Enumerated(EnumType.STRING)
    private Company company;

    private String verification;

    private Boolean isVerified;

    @Builder
    private UserCareer(Long userId, Company company, String field, String verification, Boolean isVerified) {
        this.userId = userId;
        this.company = company;
        this.field = field;
        this.verification = verification;
        this.isVerified = false;
    }

    public void changeField(String field) {
        this.field = field;
    }

    public void changeCompany(Company company) {
        this.company = company;
    }

    public void changeVerification(String verification) {
        this.verification = verification;
    }
}

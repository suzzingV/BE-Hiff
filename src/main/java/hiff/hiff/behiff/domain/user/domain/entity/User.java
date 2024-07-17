package hiff.hiff.behiff.domain.user.domain.entity;

import hiff.hiff.behiff.domain.user.domain.enums.Education;
import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import hiff.hiff.behiff.domain.user.domain.enums.Mbti;
import hiff.hiff.behiff.domain.user.domain.enums.Role;
import hiff.hiff.behiff.domain.user.domain.enums.SocialType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_TB")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    private String nickname;

    @Email
    private String email;

    @Column(length = 20)
    private String phoneNum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType;

    @Column(nullable = false)
    private String socialId;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Mbti mbti;

    @Min(0)
    private Integer income;

    private String addr1;

    private String addr2;

    private String addr3;

    @Enumerated(EnumType.STRING)
    private Education education;

    private String school;

    @Min(1900)
    @Max(2024)
    private Integer birthYear;

    @Min(1)
    @Max(12)
    private Integer birthMonth;

    @Min(1)
    @Max(31)
    private Integer birthDay;

    private Gender hopeGender;

    private String job;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    @Builder
    private User(String email, Role role,
        String socialId, SocialType socialType) {
        this.email = email;
        this.role = role;
        this.socialId = socialId;
        this.socialType = socialType;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public void changeBirthMonth(int birthMonth) {
        this.birthMonth = birthMonth;
    }

    public void changeBirthDay(int birthDay) {
        this.birthDay = birthDay;
    }

    public void changeGender(Gender gender) {
        this.gender = gender;
    }

    public void changeMbti(Mbti mbti) {
        this.mbti = mbti;
    }

    public void changeIncome(Integer income) {
        this.income = income;
    }

    public void changeAddress(String addr1, String addr2, String addr3) {
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.addr3 = addr3;
    }

    public void changeEducation(Education education, String school) {
        this.education = education;
        this.school = school;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getKey()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}

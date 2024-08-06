package hiff.hiff.behiff.domain.user.domain.entity;

import hiff.hiff.behiff.domain.user.domain.enums.*;
import hiff.hiff.behiff.domain.user.util.AgeCalculator;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_TB")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 8)
    private String nickname;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(length = 20)
    @Pattern(regexp = "^[0-9]*$")
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Income income;

    private String addr;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Education education;

    private String school;

    private LocalDate birth;

    @Min(0)
    @Max(80)
    private Integer age;

    private String job;

    @Min(20)
    @Max(50)
    private Integer hopeMinAge;

    @Min(20)
    @Max(50)
    private Integer hopeMaxAge;

    @Min(0)
    @Max(700)
    private Integer maxDistance;

    @Min(0)
    @Max(700)
    private Integer minDistance;

    @Column(nullable = false)
    private Integer heart;

    private Double evaluatedScore;

    @Column(nullable = false)
    private Integer evaluatedCount;

    private String mainPhoto;

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
        this.hopeMaxAge = 50;
        this.hopeMinAge = 20;
        this.maxDistance = 700;
        this.minDistance = 0;
        this.heart = 0;
        this.evaluatedCount = 0;
        this.evaluatedScore = 0.0;
        this.income = Income.PRIVATE;
        this.education = Education.PRIVATE;
        this.birth = LocalDate.now();
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeBirth(int birthYear, int birthMonth, int birthDay) {
        LocalDate birthDate = LocalDate.of(birthYear, birthMonth, birthDay);
        this.birth = birthDate;
        updateAge();
    }

    public void changeGender(Gender gender) {
        this.gender = gender;
    }

    public void changeMbti(Mbti mbti) {
        this.mbti = mbti;
    }

    public void changeIncome(Income income) {
        this.income = income;
    }

    public void changeAddress(String addr1, String addr2, String addr3) {
        this.addr = addr1 + " " + addr2 + " " + addr3;
    }

    public void changeEducation(Education education, String school) {
        this.education = education;
        this.school = school;
    }

    public void changeJob(String job) {
        this.job = job;
    }

    public void changePhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void changeHopeAge(Integer minAge, Integer maxAge) {
        this.hopeMinAge = minAge;
        this.hopeMaxAge = maxAge;
    }

    public void changeMaxDistance(Integer maxDistance) {
        this.maxDistance = maxDistance;
    }

    public void changeMinDistance(Integer minDistance) {
        this.minDistance = minDistance;
    }

    public void updateEvaluatedScore(Integer score) {
        int originalEvaluatedCount = this.evaluatedCount;
        double total = this.evaluatedScore * originalEvaluatedCount;
        total += score;
        this.evaluatedScore = Math.round(total / (originalEvaluatedCount + 1) * 100) / 100.0;
        this.evaluatedCount ++;
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

    public void updateAge() {
        this.age = AgeCalculator.calculateAge(this.birth);
    }
}

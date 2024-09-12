package hiff.hiff.behiff.domain.user.domain.entity;

import hiff.hiff.behiff.domain.user.domain.enums.Education;
import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import hiff.hiff.behiff.domain.user.domain.enums.Mbti;
import hiff.hiff.behiff.domain.user.domain.enums.Role;
import hiff.hiff.behiff.domain.user.domain.enums.SocialType;
import hiff.hiff.behiff.global.util.DateCalculator;
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
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
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

    @Column(length = 20)
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
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Mbti mbti;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Income income;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Education education;

    private String school;

    private LocalDate birth;

    @Min(0)
    @Max(80)
    private Integer age;

    private String career;

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
//        this.income = Income.PRIVATE;
        this.education = Education.PRIVATE;
        this.birth = LocalDate.now();
        this.gender = Gender.MALE;
        this.age = DateCalculator.calculateAge(LocalDate.now());
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

//    public void changeIncome(Income income) {
//        this.income = income;
//    }

    public void changeEducation(Education education) {
        this.education = education;
    }

    public void changeSchool(String school) {
        this.school = school;
    }

    public void changeCareer(String career) {
        this.career = career;
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
        this.evaluatedCount++;
    }

    public void updateEvaluatedScoreTmp(Double score) {
        this.evaluatedScore = score;
    }

    public void updateMainPhoto(String photoUrl) {
        this.mainPhoto = photoUrl;
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
        this.age = DateCalculator.calculateAge(this.birth);
    }

    public void addHeart(Integer amount) {
        this.heart += amount;
    }

    public void subtractHeart(Integer amount) {
        this.heart -= amount;
    }
}

package hiff.hiff.behiff.domain.plan.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "user_plan_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Integer coupon;

    private Boolean isMembership;

    private LocalDateTime membershipStartDate;

    @Builder
    private UserPlan(Long userId) {
        this.userId = userId;
        this.coupon = 0;
        this.isMembership = false;
    }

    public void addCoupon(int amount) {
        this.coupon += amount;
    }

    public void subtractCoupon() {
        this.coupon --;
    }

    public void joinMembership() {
        this.isMembership = true;
        this.membershipStartDate = LocalDateTime.now();
    }

    public void cancelMembership() {
        this.isMembership = false;
        this.membershipStartDate = null;
    }
}

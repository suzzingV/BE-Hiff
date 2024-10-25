package hiff.hiff.behiff.domain.plan.domain.entity;

import hiff.hiff.behiff.domain.plan.domain.enums.Plan;
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
@Getter
@Table(name = "user_plan_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Integer point;

    @Enumerated(EnumType.STRING)
    private Plan plan;

    private Integer matchingCnt;

    private Integer rejectionCnt;

    @Builder
    private UserPlan(Long userId) {
        this.userId = userId;
        this.point = 3;
        this.plan = Plan.BASIC;
        this.matchingCnt = 3;
        this.rejectionCnt = 0;
    }

    public void updatePoint(Integer amount) {
        this.point = amount;
    }

    public void changePlan(Plan plan) {
        this.plan = plan;
    }

    public void updateMatchingCnt(int amount) {
        this.matchingCnt = amount;
    }

    public void updateRejectionCnt(int amount) {
        this.rejectionCnt = amount;
    }
}

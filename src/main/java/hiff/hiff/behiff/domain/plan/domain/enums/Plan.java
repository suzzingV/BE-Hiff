package hiff.hiff.behiff.domain.plan.domain.enums;

import hiff.hiff.behiff.domain.user.domain.strategy.BasicPlan;
import hiff.hiff.behiff.domain.user.domain.strategy.EssentialPlan;
import hiff.hiff.behiff.domain.user.domain.strategy.PlanStrategy;
import hiff.hiff.behiff.domain.user.domain.strategy.PlusPlan;
import hiff.hiff.behiff.domain.user.domain.strategy.PrimePlan;
import java.util.EnumSet;
import java.util.Set;

public enum Plan {
    BASIC(new BasicPlan()), ESSENTIAL(new EssentialPlan()), PLUS(new PlusPlan()), PRIME(new PrimePlan());

    private final PlanStrategy planStrategy;
    public static final Set<Plan> ALL_VALUES = EnumSet.allOf(Plan.class);

    Plan(PlanStrategy planStrategy) {
        this.planStrategy = planStrategy;
    }

    public PlanStrategy getPlanStrategy() {
        return planStrategy;
    }
}

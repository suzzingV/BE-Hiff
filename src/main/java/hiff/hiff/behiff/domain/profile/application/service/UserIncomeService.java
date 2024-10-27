package hiff.hiff.behiff.domain.profile.application.service;

import hiff.hiff.behiff.domain.profile.domain.entity.UserIncome;
import hiff.hiff.behiff.domain.profile.infrastructure.UserIncomeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserIncomeService {

    private final UserIncomeRepository userIncomeRepository;

    public void createIncome(Long userId, Integer income, String verification) {
        UserIncome userIncome = UserIncome.builder()
            .userId(userId)
            .income(income)
            .verification(verification)
            .build();
        userIncomeRepository.save(userIncome);
    }

    public UserIncome findByUserId(Long userId) {
        return userIncomeRepository.findByUserId(userId)
            .orElse(UserIncome.builder().build());
    }

    public void deleteByUserId(Long userId) {
        userIncomeRepository.deleteByUserId(userId);
    }
}

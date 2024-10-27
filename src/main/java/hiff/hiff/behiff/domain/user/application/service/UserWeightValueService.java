package hiff.hiff.behiff.domain.user.application.service;

import hiff.hiff.behiff.domain.weighting.domain.entity.Weighting;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.WeightValueRepository;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserWeightValueService {

    private final WeightValueRepository weightValueRepository;

    public void updateWeightValue(Long userId, Integer appearanceWV, Integer hobbyWV,
        Integer lifeStyleWV, Integer mbtiWV) {
        Weighting weighting = weightValueRepository.findByUserId(userId)
            .orElseThrow(() -> new UserException(ErrorCode.WEIGHT_VALUE_NOT_FOUND));
//        checkIncomePrivate(userId, weightValue);
        weighting.changeWeightValue(appearanceWV, hobbyWV, lifeStyleWV, mbtiWV);
    }

    public Weighting createWeightValue(Long userId) {
        Weighting weighting = Weighting.builder()
            .userId(userId)
            .build();
        return weightValueRepository.save(weighting);
    }

    public Weighting findByUserId(Long userId) {
        return weightValueRepository.findByUserId(userId)
            .orElse(Weighting.builder().build());
    }

//    private void checkIncomePrivate(Long userId, WeightValue weightValue) {
//        User user = userCRUDService.findById(userId);
//        if (user.getIncome() == Income.PRIVATE && weightValue.getIncome() != 0) {
//            throw new UserException(ErrorCode.INCOME_WEIGHT_VALUE_PRIVATE);
//        }
//    }
}

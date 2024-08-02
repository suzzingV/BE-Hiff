package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.user.domain.entity.WeightValue;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.WeightValueRepository;
import hiff.hiff.behiff.domain.user.presentation.dto.req.WeightValueRequest;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserWeightValueService {

    private final WeightValueRepository weightValueRepository;

    public void updateWeightValue(Long userId, WeightValueRequest request) {
        weightValueRepository.findByUserId(userId)
                .ifPresentOrElse(weightValue ->
                                weightValue.changeWeightValue(request.getIncome(), request.getAppearance(), request.getHobby(),
                                        request.getBelief(), request.getMbti()), () -> {
                            WeightValue weightValue = WeightValue.builder()
                                    .userId(userId)
                                    .income(request.getIncome())
                                    .appearance(request.getAppearance())
                                    .hobby(request.getHobby())
                                    .belief(request.getBelief())
                                    .mbti(request.getMbti())
                                    .build();
                            weightValueRepository.save(weightValue);
                        }
                );
    }

    public WeightValue findByUserId(Long userId) {
        return weightValueRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(ErrorCode.WEIGHT_VALUE_NOT_FOUND));
    }
}

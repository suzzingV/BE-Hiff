package hiff.hiff.behiff.domain.user.application.service;

import hiff.hiff.behiff.domain.user.domain.entity.Career;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.CareerRepository;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCareerService {

    private final CareerRepository careerRepository;

    public void updateOriginCareer(User user, Long careerId) {
        Career career = findById(careerId);
        user.changeCareer(career.getName());
        career.addCount();
    }

    public void updateNewCareer(User user, String careerName) {
        careerRepository.findByName(careerName)
            .ifPresentOrElse(Career::addCount, () -> {
                Career career = Career.builder()
                    .name(careerName)
                    .build();
                careerRepository.save(career);
            });
        user.changeCareer(careerName);
    }

    private Career findById(Long careerId) {
        return careerRepository.findById(careerId)
            .orElseThrow(() -> new UserException(ErrorCode.CAREER_NOT_FOUND));
    }

    public List<Career> getAllCareers() {
        return careerRepository.findAll();
    }
}

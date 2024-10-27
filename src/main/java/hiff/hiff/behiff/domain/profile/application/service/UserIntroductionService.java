package hiff.hiff.behiff.domain.profile.application.service;

import hiff.hiff.behiff.domain.catalog.application.service.CatalogIntroductionService;
import hiff.hiff.behiff.domain.catalog.domain.entity.Question;
import hiff.hiff.behiff.domain.profile.application.dto.UserIntroductionDto;
import hiff.hiff.behiff.domain.profile.domain.entity.UserIntroduction;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.profile.infrastructure.UserIntroductionRepository;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.UserQuestionRequest;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserIntroductionService {

    private final UserIntroductionRepository userIntroductionRepository;
    private final CatalogIntroductionService catalogIntroductionService;

    public void updateIntroduction(Long userId, Long questionId, String content) {
        UserIntroduction userIntroduction = userIntroductionRepository.findByUserIdAndQuestionId(
                userId, questionId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_INTRODUCTION_NOT_FOUND));
        userIntroduction.changeContent(content);
    }

    public List<UserIntroductionDto> findIntroductionByUserId(Long userId) {
        return userIntroductionRepository.findByUserId(userId)
            .stream().map(userIntroduction -> {
                Question question = catalogIntroductionService.findQuestionById(userIntroduction);
                return UserIntroductionDto.builder()
                    .question(question.getQuestion())
                    .content(userIntroduction.getContent())
                    .build();
            }).toList();
    }

    public void registerUserIntroduction(Long userId, UserQuestionRequest request) {
        List<Long> questionIds = request.getQuestionIds();
        userIntroductionRepository.findByUserId(userId)
            .forEach(userIntroduction -> {
                Long questionId = userIntroduction.getQuestionId();
                if (!questionIds.contains(questionId)) {
                    userIntroductionRepository.delete(userIntroduction);
                } else {
                    questionIds.remove(questionId);
                }
            });
        questionIds.forEach(questionId -> {
            UserIntroduction userIntroduction = UserIntroduction.builder()
                .questionId(questionId)
                .userId(userId)
                .build();
            userIntroductionRepository.save(userIntroduction);
        });
    }

    public void deleteByUserId(Long userId) {
        userIntroductionRepository.deleteByUserId(userId);
    }
}

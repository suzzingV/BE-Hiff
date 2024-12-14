package hiff.hiff.behiff.domain.profile.application.service;

import hiff.hiff.behiff.domain.catalog.application.service.CatalogIntroductionService;
import hiff.hiff.behiff.domain.catalog.domain.entity.Question;
import hiff.hiff.behiff.domain.catalog.infrastructure.QuestionRepository;
import hiff.hiff.behiff.domain.profile.application.dto.UserIntroductionDto;
import hiff.hiff.behiff.domain.profile.domain.entity.UserIntroduction;
import hiff.hiff.behiff.domain.profile.exception.ProfileException;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.profile.infrastructure.UserIntroductionRepository;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.UserQuestionRequest;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserIntroductionService {

    private final UserIntroductionRepository userIntroductionRepository;
    private final CatalogIntroductionService catalogIntroductionService;

    public void updateIntroduction(Long userId, Long questionId, String content) {
        Question question = catalogIntroductionService.findQuestionById(questionId);
        UserIntroduction userIntroduction = findUserIdAndQuestionId(userId, questionId);
        userIntroduction.updateQuestion(question.getQuestion());
        userIntroduction.updateContent(content);
    }

    public List<UserIntroductionDto> findIntroductionByUserId(Long userId) {
        return userIntroductionRepository.findByUserId(userId)
            .stream().map(userIntroduction -> {
                Question question = catalogIntroductionService.findQuestionById(userIntroduction.getQuestionId());
                return UserIntroductionDto.builder()
                    .question(question.getQuestion())
                    .content(userIntroduction.getContent())
                    .build();
            }).toList();
    }

    public void registerUserIntroduction(Long userId, UserQuestionRequest request) {
        List<Long> questionIds = request.getQuestionIds();
        deleteOldIntroductions(userId, questionIds);
        questionIds.stream()
                .filter(questionId -> !isExists(userId, questionId))
                .forEach(questionId -> {
            Question question = catalogIntroductionService.findQuestionById(questionId);
            UserIntroduction userIntroduction = UserIntroduction.builder()
                .questionId(questionId)
                    .question(question.getQuestion())
                .userId(userId)
                .build();
            userIntroductionRepository.save(userIntroduction);
        });
    }

    public void deleteByUserId(Long userId) {
        userIntroductionRepository.deleteByUserId(userId);
    }

    private boolean isExists(Long userId, Long questionId) {
        return userIntroductionRepository.findByUserIdAndQuestionId(userId, questionId).isPresent();
    }

    private void deleteOldIntroductions(Long userId, List<Long> questionIds) {
        userIntroductionRepository.findByUserId(userId)
            .forEach(userIntroduction -> {
                Long questionId = userIntroduction.getQuestionId();
                if (!questionIds.contains(questionId)) {
                    userIntroductionRepository.delete(userIntroduction);
                } else {
                    questionIds.remove(questionId);
                }
            });
    }

    private UserIntroduction findUserIdAndQuestionId(Long userId, Long questionId) {
        return userIntroductionRepository.findByUserIdAndQuestionId(userId, questionId)
                .orElseThrow(() -> new ProfileException(ErrorCode.USER_INTRODUCTION_NOT_FOUND));
    }
}

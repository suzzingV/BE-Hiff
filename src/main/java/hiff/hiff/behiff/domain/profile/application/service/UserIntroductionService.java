package hiff.hiff.behiff.domain.profile.application.service;

import hiff.hiff.behiff.domain.catalog.application.service.CatalogIntroductionService;
import hiff.hiff.behiff.domain.catalog.domain.entity.Question;
import hiff.hiff.behiff.domain.profile.application.dto.UserIntroductionDto;
import hiff.hiff.behiff.domain.profile.domain.entity.UserIntroduction;
import hiff.hiff.behiff.domain.profile.exception.ProfileException;
import hiff.hiff.behiff.domain.profile.infrastructure.UserIntroductionRepository;
import hiff.hiff.behiff.domain.profile.presentation.dto.req.UserQuestionRequest;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserIntroductionService {

    private final UserIntroductionRepository userIntroductionRepository;
    private final CatalogIntroductionService catalogIntroductionService;

    public void updateIntroduction(Long userId, Long questionId, String question, String content) {
        UserIntroduction userIntroduction = findByUserIdAndQuestionId(userId, questionId);
        userIntroduction.updateQuestion(question);
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

    public void registerUserIntroduction(Long userId, Long questionId, String question, String content) {
        if(isExists(userId, questionId)) {
            throw new ProfileException(ErrorCode.INTRODUCTION_ALREADY_EXISTS);
        }
            UserIntroduction userIntroduction = UserIntroduction.builder()
                .questionId(questionId)
                    .question(question)
                    .content(content)
                .userId(userId)
                .build();
            userIntroductionRepository.save(userIntroduction);
    }

    public void deleteByUserId(Long userId) {
        userIntroductionRepository.deleteByUserId(userId);
    }

    private boolean isExists(Long userId, Long questionId) {
        return userIntroductionRepository.findByUserIdAndQuestionId(userId, questionId).isPresent();
    }

    private UserIntroduction findByUserIdAndQuestionId(Long userId, Long questionId) {
        return userIntroductionRepository.findByUserIdAndQuestionId(userId, questionId)
                .orElseThrow(() -> new ProfileException(ErrorCode.USER_INTRODUCTION_NOT_FOUND));
    }
}

package hiff.hiff.behiff.domain.catalog.application.service;

import hiff.hiff.behiff.domain.catalog.domain.entity.Question;
import hiff.hiff.behiff.domain.catalog.exception.CatalogException;
import hiff.hiff.behiff.domain.catalog.infrastructure.QuestionRepository;
import hiff.hiff.behiff.domain.profile.application.dto.UserIntroductionDto;
import hiff.hiff.behiff.domain.profile.application.service.UserIntroductionService;
import hiff.hiff.behiff.domain.profile.application.service.UserProfileService;
import hiff.hiff.behiff.domain.profile.domain.entity.UserIntroduction;
import hiff.hiff.behiff.domain.profile.infrastructure.UserIntroductionRepository;
import hiff.hiff.behiff.global.response.properties.ErrorCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CatalogIntroductionService {

    private final QuestionRepository questionRepository;
    private final UserIntroductionRepository userIntroductionRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question findQuestionById(Long questionId) {
        return questionRepository.findById(questionId)
            .orElseThrow(() -> new CatalogException(ErrorCode.QUESTION_NOT_FOUND));
    }

    public List<Question> getNotSelectedQuestions(Long userId) {
        List<Long> selectedQuestionIds = findIntroductionByUserId(userId).stream()
                .map(UserIntroduction::getQuestionId).toList();
        return getAllQuestions().stream()
                .filter(question -> !selectedQuestionIds.contains(question.getId()))
                .toList();
    }

    public List<UserIntroduction> findIntroductionByUserId(Long userId) {
        return userIntroductionRepository.findByUserId(userId);
    }
}

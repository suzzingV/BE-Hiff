package hiff.hiff.behiff.domain.catalog.application.service;

import hiff.hiff.behiff.domain.catalog.domain.entity.Question;
import hiff.hiff.behiff.domain.catalog.exception.CatalogException;
import hiff.hiff.behiff.domain.catalog.infrastructure.QuestionRepository;
import hiff.hiff.behiff.domain.profile.domain.entity.UserIntroduction;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
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

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question findQuestionById(UserIntroduction userIntroduction) {
        return questionRepository.findById(userIntroduction.getQuestionId())
            .orElseThrow(() -> new CatalogException(ErrorCode.QUESTION_NOT_FOUND));
    }
}

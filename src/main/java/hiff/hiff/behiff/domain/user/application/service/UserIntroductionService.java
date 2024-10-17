package hiff.hiff.behiff.domain.user.application.service;

import hiff.hiff.behiff.domain.user.application.dto.UserIntroductionDto;
import hiff.hiff.behiff.domain.user.domain.entity.Question;
import hiff.hiff.behiff.domain.user.domain.entity.UserIntroduction;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.QuestionRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserIntroductionRepository;
import hiff.hiff.behiff.domain.user.presentation.dto.req.UserQuestionRequest;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserIntroductionService {

    private final QuestionRepository questionRepository;
    private final UserIntroductionRepository userIntroductionRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public void updateIntroduction(Long userId, Long questionId, String content) {
        UserIntroduction userIntroduction = userIntroductionRepository.findByUserIdAndQuestionId(
                userId, questionId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_INTRODUCTION_NOT_FOUND));
        userIntroduction.changeContent(content);
    }

    public List<UserIntroductionDto> findIntroductionByUserId(Long userId) {
         return userIntroductionRepository.findByUserId(userId)
            .stream().map(userIntroduction -> {
                Question question = findQuestionById(userIntroduction);
                return UserIntroductionDto.builder()
                    .question(question.getQuestion())
                    .content(userIntroduction.getContent())
                    .build();
            }).toList();
    }

    private Question findQuestionById(UserIntroduction userIntroduction) {
        return questionRepository.findById(userIntroduction.getQuestionId())
            .orElseThrow(() -> new UserException(ErrorCode.QUESTION_NOT_FOUND));
    }

    public void registerUserIntroduction(Long userId, UserQuestionRequest request) {
        List<Long> questionIds = request.getQuestionIds();
        userIntroductionRepository.findByUserId(userId)
            .forEach(userIntroduction -> {
                Long questionId = userIntroduction.getQuestionId();
                if(!questionIds.contains(questionId)) {
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
}

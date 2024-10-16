package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.user.domain.entity.Question;
import hiff.hiff.behiff.domain.user.infrastructure.QuestionRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserQuestionService {

    private final QuestionRepository questionRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }
}

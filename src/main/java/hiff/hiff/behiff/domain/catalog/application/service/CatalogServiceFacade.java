package hiff.hiff.behiff.domain.catalog.application.service;

import hiff.hiff.behiff.domain.catalog.presentation.dto.res.QuestionResponse;
import hiff.hiff.behiff.domain.catalog.presentation.dto.res.TagResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CatalogServiceFacade {

    private final CatalogHobbyService catalogHobbyService;
    private final CatalogLifeStyleService catalogLifeStyleService;
    private final CatalogIntroductionService catalogIntroductionService;
    private final CatalogFieldService catalogFieldService;

    public List<TagResponse> getAllHobbies() {
        return catalogHobbyService.getAllHobbies()
            .stream().map(hobby ->
                TagResponse.builder()
                    .id(hobby.getId())
                    .name(hobby.getName())
                    .build())
            .toList();
    }

    @Transactional(readOnly = true)
    public List<TagResponse> getAllLifeStyles() {
        return catalogLifeStyleService.getAllLifeStyles()
            .stream().map(lifeStyle ->
                TagResponse.builder()
                    .id(lifeStyle.getId())
                    .name(lifeStyle.getName())
                    .build())
            .toList();
    }

    public List<QuestionResponse> getQuestionList() {
        return catalogIntroductionService.getAllQuestions()
            .stream().map(question ->
                QuestionResponse.builder()
                    .id(question.getId())
                    .question(question.getQuestion())
                    .build())
            .toList();
    }

    @Transactional(readOnly = true)
    public List<TagResponse> getFields() {
        return catalogFieldService.getAllFields()
            .stream().map(career ->
                TagResponse.builder()
                    .id(career.getId())
                    .name(career.getName())
                    .build())
            .toList();
    }
}

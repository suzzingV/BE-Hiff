package hiff.hiff.behiff.domain.catalog.presentation.controller;

import hiff.hiff.behiff.domain.catalog.application.service.CatalogServiceFacade;
import hiff.hiff.behiff.domain.catalog.presentation.dto.res.QuestionResponse;
import hiff.hiff.behiff.domain.catalog.presentation.dto.res.TagResponse;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Catalog", description = "Catalog 관련 API")
@RestController
@RequestMapping("/api/v0.2/catalog")
@RequiredArgsConstructor
public class CatalogControllerV02 {

    private final CatalogServiceFacade catalogServiceFacade;

    @Operation(
        summary = "자기소개 질문 목록 조회",
        description = "자기소개 질문 목록을 조회합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "자기소개 질문 목록 조회에 성공하였습니다."
    )
    @GetMapping("/question/list")
    public ResponseEntity<List<QuestionResponse>> getQuestionList(
        @AuthenticationPrincipal User user) {
        List<QuestionResponse> response = catalogServiceFacade.getQuestionList();
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "직업 목록 조회",
        description = "직업 목록을 조회합니다. 토큰 x"
    )
    @ApiResponse(
        responseCode = "200",
        description = "직업 목록 조회에 성공하였습니다."
    )
    @GetMapping("/field/list")
    public ResponseEntity<List<TagResponse>> getFields() {
        List<TagResponse> responses = catalogServiceFacade.getFields();
        return ResponseEntity.ok(responses);
    }

    @Operation(
        summary = "취미 목록 조회",
        description = "취미 목록을 조회합니다. 토큰 x"
    )
    @ApiResponse(
        responseCode = "200",
        description = "취미 목록 조회에 성공하였습니다."
    )
    @GetMapping("/hobby/list")
    public ResponseEntity<List<TagResponse>> getHobbies() {
        List<TagResponse> responses = catalogServiceFacade.getAllHobbies();
        return ResponseEntity.ok(responses);
    }

    @Operation(
        summary = "라이프스타일 목록 조회",
        description = "라이프스타일 목록을 조회합니다. 토큰 x"
    )
    @ApiResponse(
        responseCode = "200",
        description = "라이프스타일 목록 조회에 성공하였습니다."
    )
    @GetMapping("/life-style/list")
    public ResponseEntity<List<TagResponse>> getLifeStyles() {
        List<TagResponse> responses = catalogServiceFacade.getAllLifeStyles();
        return ResponseEntity.ok(responses);
    }

    @Operation(
        summary = "대학교 목록 조회",
        description = "대학교 목록을 조회합니다. 토큰 x"
    )
    @ApiResponse(
        responseCode = "200",
        description = "대학교 목록 조회에 성공하였습니다."
    )
    @GetMapping("/university/list")
    public ResponseEntity<List<TagResponse>> getUniversities() {
        List<TagResponse> responses = catalogServiceFacade.getAllUniversities();
        return ResponseEntity.ok(responses);
    }
}

package hiff.hiff.behiff.domain.chat.presentation.controller;

import hiff.hiff.behiff.domain.chat.application.ChatService;
import hiff.hiff.behiff.domain.chat.presentation.dto.req.ChatAcceptanceRequest;
import hiff.hiff.behiff.domain.chat.presentation.dto.req.ChatProposalRequest;
import hiff.hiff.behiff.domain.chat.presentation.dto.res.ChatProposalResponse;
import hiff.hiff.behiff.domain.chat.presentation.dto.res.ChatProposedResponse;
import hiff.hiff.behiff.domain.chat.presentation.dto.res.ChatProposerResponse;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Chat", description = "Chat 관련 API")
@RestController
@RequestMapping("/api/v0/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @Operation(
            summary = "대화 신청",
            description = "대화를 신청합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "대화 신청에 성공하였습니다."
    )
    @PostMapping("/proposal")
    public ResponseEntity<ChatProposalResponse> propose(@AuthenticationPrincipal User user, @RequestBody @Valid ChatProposalRequest request) {
        ChatProposalResponse response = chatService.proposeChat(user, request.getMatchedId());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "받은 대화 신청 내역 조회.",
            description = "받은 대화 신청 내역을 조회합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "받은 대화 신청 내역 조회에 성공하였습니다."
    )
    @GetMapping("/proposed")
    public ResponseEntity<List<ChatProposerResponse>> getProposedList(@AuthenticationPrincipal User user) {
        List<ChatProposerResponse> responses = chatService.getProposedList(user.getId());

        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "보낸 대화 신청 내역 조회",
            description = "보낸 대화 신청 내역을 조회합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "보낸 대화 신청 내역 조회에 성공하였습니다."
    )
    @GetMapping("/proposal")
    public ResponseEntity<List<ChatProposedResponse>> getProposalList(@AuthenticationPrincipal User user) {
        List<ChatProposedResponse> responses = chatService.getProposalList(user.getId());

        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "대화 신청 수락.",
            description = "대화 신청을 수락합니다.합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "대화 신청 수락에 성공하였습니다."
    )
    @PostMapping("/acceptance")
    public ResponseEntity<ChatProposalResponse> accept(@AuthenticationPrincipal User user, @RequestBody @Valid ChatAcceptanceRequest request) {
        ChatProposalResponse response = chatService.acceptProposal(user, request);

        return ResponseEntity.ok(response);
    }
}

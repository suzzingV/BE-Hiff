package hiff.hiff.behiff.domain.bond.presentation.controller;

import hiff.hiff.behiff.domain.bond.application.BondService;
import hiff.hiff.behiff.domain.bond.presentation.dto.res.ChatAcceptanceResponse;
import hiff.hiff.behiff.domain.bond.presentation.dto.res.ChatSendingResponse;
import hiff.hiff.behiff.domain.bond.presentation.dto.res.LikeResponse;
import hiff.hiff.behiff.domain.bond.presentation.dto.res.LikeToUserResponse;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Bond", description = "Bond 관련 API")
@RestController
@RequestMapping("/api/v0.2/bond")
@RequiredArgsConstructor
public class BondController {

    private final BondService bondService;

    @Operation(
        summary = "호감 보내기",
        description = "호감을 보냅니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "호감 보내기에 성공하였습니다."
    )
    @PostMapping("/like/{responderId}")
    public ResponseEntity<LikeResponse> sendLike(@AuthenticationPrincipal User user, @PathVariable Long responderId) {
        LikeResponse response = bondService.sendLike(user.getId(), responderId);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "호감을 보낸 상대 조회",
            description = "호감을 보낸 상대를 조회합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "호감을 보낸 상대 조회에 성공하였습니다."
    )
    @GetMapping("/like/list/send")
    public ResponseEntity<List<LikeToUserResponse>> getUsersOfSendingLike(@AuthenticationPrincipal User user) {
        List<LikeToUserResponse> responses = bondService.getUsersOfSendingLike(user.getId());

        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "호감을 받은 상대 조회",
            description = "호감을 받은 상대를 조회합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "호감을 받은 상대 조회에 성공하였습니다."
    )
    @GetMapping("/like/list/received")
    public ResponseEntity<List<LikeToUserResponse>> getLikers(@AuthenticationPrincipal User user) {
        List<LikeToUserResponse> responses = bondService.getLikers(user.getId());

        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "매칭 신청",
            description = "매칭을 신청합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "매칭 신청에 성공하였습니다."
    )
    @PostMapping("/chat/send/{responderId}")
    public ResponseEntity<ChatSendingResponse> sendChat(@AuthenticationPrincipal User user, @PathVariable Long responderId) {
        ChatSendingResponse response = bondService.sendChat(user.getId(), responderId);

        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "매칭 수락",
            description = "매칭을 수락합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "매칭 수락에 성공하였습니다."
    )
    @PostMapping("/chat/accept/{senderId}")
    public ResponseEntity<ChatAcceptanceResponse> acceptChat(@AuthenticationPrincipal User user, @PathVariable Long senderId) {
        ChatAcceptanceResponse response = bondService.acceptChat(user.getId(), senderId);

        return ResponseEntity.ok(response);
    }
//
//    @Operation(
//        summary = "받은 대화 신청 내역 조회.",
//        description = "받은 대화 신청 내역을 조회합니다. 토큰 o"
//    )
//    @ApiResponse(
//        responseCode = "200",
//        description = "받은 대화 신청 내역 조회에 성공하였습니다."
//    )
//    @GetMapping("/proposed")
//    public ResponseEntity<List<ChatProposerResponse>> getProposedList(
//        @AuthenticationPrincipal User user) {
//        List<ChatProposerResponse> responses = chatService.getProposedList(user.getId());
//
//        return ResponseEntity.ok(responses);
//    }
//
//    @Operation(
//        summary = "보낸 대화 신청 내역 조회",
//        description = "보낸 대화 신청 내역을 조회합니다. 토큰 o"
//    )
//    @ApiResponse(
//        responseCode = "200",
//        description = "보낸 대화 신청 내역 조회에 성공하였습니다."
//    )
//    @GetMapping("/proposal")
//    public ResponseEntity<List<ChatProposedResponse>> getProposalList(
//        @AuthenticationPrincipal User user) {
//        List<ChatProposedResponse> responses = chatService.getProposalList(user.getId());
//
//        return ResponseEntity.ok(responses);
//    }
//
//    @Operation(
//        summary = "대화 신청 수락.",
//        description = "대화 신청을 수락합니다.합니다. 토큰 o"
//    )
//    @ApiResponse(
//        responseCode = "200",
//        description = "대화 신청 수락에 성공하였습니다."
//    )
//    @PostMapping("/acceptance")
//    public ResponseEntity<ChatProposalResponse> accept(@AuthenticationPrincipal User user,
//        @RequestBody @Valid ChatAcceptanceRequest request) {
//        ChatProposalResponse response = chatService.acceptProposal(user.getId(), request);
//
//        return ResponseEntity.ok(response);
//    }
}

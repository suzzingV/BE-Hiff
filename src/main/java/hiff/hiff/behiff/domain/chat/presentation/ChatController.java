package hiff.hiff.behiff.domain.chat.presentation;

import hiff.hiff.behiff.domain.chat.application.ChatService;
import hiff.hiff.behiff.domain.chat.presentation.dto.req.ChatAcceptanceRequest;
import hiff.hiff.behiff.domain.chat.presentation.dto.req.ChatProposalRequest;
import hiff.hiff.behiff.domain.chat.presentation.dto.res.ChatProposalResponse;
import hiff.hiff.behiff.domain.chat.presentation.dto.res.ChatProposedResponse;
import hiff.hiff.behiff.domain.chat.presentation.dto.res.ChatProposerResponse;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/proposal")
    public ResponseEntity<ChatProposalResponse> propose(@AuthenticationPrincipal User user, @RequestBody ChatProposalRequest request) {
        ChatProposalResponse response = chatService.proposeChat(user, request.getMatchedId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/proposed")
    public ResponseEntity<List<ChatProposerResponse>> getProposedList(@AuthenticationPrincipal User user) {
        List<ChatProposerResponse> responses = chatService.getProposedList(user.getId());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/proposal")
    public ResponseEntity<List<ChatProposedResponse>> getProposalList(@AuthenticationPrincipal User user) {
        List<ChatProposedResponse> responses = chatService.getProposalList(user.getId());

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/acceptance")
    public ResponseEntity<ChatProposalResponse> accept(@AuthenticationPrincipal User user, @RequestBody ChatAcceptanceRequest request) {
        ChatProposalResponse response = chatService.acceptProposal(user, request);

        return ResponseEntity.ok(response);
    }
}

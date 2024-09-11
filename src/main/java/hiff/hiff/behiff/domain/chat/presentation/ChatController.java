package hiff.hiff.behiff.domain.chat.presentation;

import hiff.hiff.behiff.domain.chat.application.ChatService;
import hiff.hiff.behiff.domain.chat.presentation.dto.req.ChatProposalRequest;
import hiff.hiff.behiff.domain.chat.presentation.dto.res.ChatProposalResponse;
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
    public ResponseEntity<Void> propose(@AuthenticationPrincipal User user, @RequestBody ChatProposalRequest request) {
        chatService.proposeChat(user, request.getMatchedId());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/proposed")
    public ResponseEntity<List<ChatProposalResponse>> getProposedList(@AuthenticationPrincipal User user) {
        List<ChatProposalResponse> responses = chatService.getProposedList(user.getId());

        return ResponseEntity.ok(responses);
    }
}

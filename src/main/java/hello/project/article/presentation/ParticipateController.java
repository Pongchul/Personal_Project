package hello.project.article.presentation;

import hello.project.article.service.ParticipateService;
import hello.project.auth.config.Authenticated;
import hello.project.auth.config.AuthenticationPrincipal;
import hello.project.member.service.dto.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/articles/{articleId}/participants")
@RestController
public class ParticipateController {

    private final ParticipateService participateService;

    @Authenticated
    @PostMapping
    public ResponseEntity<Void> participate(@AuthenticationPrincipal Long memberId, @PathVariable Long articleId) {
        participateService.participate(articleId, memberId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> findParticipants(@PathVariable Long articleId) {
        List<MemberResponse> responses = participateService.findParticipants(articleId);
        return ResponseEntity.ok(responses);
    }

    @Authenticated
    @DeleteMapping
    public ResponseEntity<Void> leave(@AuthenticationPrincipal Long memberId, @PathVariable Long articleId) {
        participateService.leave(memberId, articleId);
        return ResponseEntity.noContent().build();
    }
}

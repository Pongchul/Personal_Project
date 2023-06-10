package hello.project.favorite.presentation;

import hello.project.auth.config.Authenticated;
import hello.project.auth.config.AuthenticationPrincipal;
import hello.project.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/articles/{articleId}/{like}")
@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Authenticated
    @PostMapping
    public ResponseEntity<Void> like(@AuthenticationPrincipal Long memberId, @PathVariable Long articleId) {
        favoriteService.like(articleId, memberId);
        return ResponseEntity.ok().build();
    }

    @Authenticated
    @DeleteMapping
    public ResponseEntity<Void> leave(@AuthenticationPrincipal Long memberId, @PathVariable Long articleId) {
        favoriteService.cancel(articleId, memberId);
        return ResponseEntity.noContent().build();
    }
}

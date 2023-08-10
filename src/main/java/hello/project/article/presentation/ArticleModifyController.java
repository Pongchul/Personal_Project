package hello.project.article.presentation;

import hello.project.article.presentation.dto.request.ArticleApiRequest;
import hello.project.article.presentation.dto.request.ArticleRequestAssembler;
import hello.project.article.service.ArticleModifyService;
import hello.project.article.service.dto.response.ArticleIdResponse;
import hello.project.auth.config.Authenticated;
import hello.project.auth.config.AuthenticationPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@Authenticated
@RequiredArgsConstructor
@RequestMapping("/api/articles")
@RestController
public class ArticleModifyController {

    private final ArticleRequestAssembler articleRequestAssembler;
    private final ArticleModifyService articleModifyService;


    @PostMapping
    public ResponseEntity<ArticleIdResponse> create(@AuthenticationPrincipal Long memberId,
                                                    @RequestBody @Valid ArticleApiRequest request) {
        ArticleIdResponse response = articleModifyService.create(memberId, articleRequestAssembler.articleRequest(request));
        URI uri = URI.create("/api/articles/" + response.getArticleId());
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<Void> update(@AuthenticationPrincipal Long memberId, @PathVariable Long articleId,
                                       @RequestBody @Valid ArticleApiRequest request) {
        articleModifyService.update(memberId, articleId, articleRequestAssembler.articleRequest(request));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{articleId}/close")
    public ResponseEntity<Void> closeEarly(@AuthenticationPrincipal Long memberId, @PathVariable Long articleId) {
        articleModifyService.closeEarly(memberId, articleId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Long memberId, @PathVariable Long articleId) {
        articleModifyService.delete(memberId, articleId);
        return ResponseEntity.noContent().build();
    }
}

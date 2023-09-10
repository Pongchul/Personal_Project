package hello.project.article.presentation;

import hello.project.article.service.ArticleSearchService;
import hello.project.article.service.dto.request.ArticleSearchRequest;
import hello.project.article.service.dto.response.ArticlePageResponse;
import hello.project.article.service.dto.response.ArticleResponse;
import hello.project.auth.config.Authenticated;
import hello.project.auth.config.AuthenticationPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/articles")
public class ArticleSearchController {

    private final ArticleSearchService articleSearchService;

    @Authenticated
    @GetMapping(value = "/{articleId}", headers = HttpHeaders.AUTHORIZATION)
    public ResponseEntity<ArticleResponse> findArticle(@AuthenticationPrincipal Long memberId,
                                                       @PathVariable Long articleId) {
        ArticleResponse response = articleSearchService.findArticle(articleId, memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{articleId}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable Long articleId) {
        ArticleResponse response = articleSearchService.findArticle(articleId);

        return ResponseEntity.ok(response);
    }

    @Authenticated
    @GetMapping(headers = HttpHeaders.AUTHORIZATION)
    public ResponseEntity<ArticlePageResponse> findAll(@AuthenticationPrincipal Long memberId,
                                                       @ModelAttribute ArticleSearchRequest articleSearchRequest) {
        ArticlePageResponse response = articleSearchService.findArticles(articleSearchRequest, memberId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ArticlePageResponse> findAll(@ModelAttribute ArticleSearchRequest articleSearchRequest) {
        ArticlePageResponse response = articleSearchService.findArticles(articleSearchRequest);

        return ResponseEntity.ok(response);
    }

    @Authenticated
    @GetMapping("/my/participated")
    public ResponseEntity<ArticlePageResponse> findParticipantsArticles(@AuthenticationPrincipal Long memberId,
                                                                        @ModelAttribute ArticleSearchRequest articleSearchRequest) {
        ArticlePageResponse response = articleSearchService.findParticipatedArticles(articleSearchRequest, memberId);

        return ResponseEntity.ok(response);
    }

    @Authenticated
    @GetMapping("/my/hosted")
    public ResponseEntity<ArticlePageResponse> findHostedArticles(@AuthenticationPrincipal Long memberId,
                                                                  @ModelAttribute ArticleSearchRequest articleSearchRequest) {
        ArticlePageResponse response = articleSearchService.findHostedArticles(articleSearchRequest, memberId);

        return ResponseEntity.ok(response);
    }
}

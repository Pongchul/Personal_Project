package hello.project.article.domain.search;


import hello.project.article.domain.search.dto.ArticleSummaryRepositoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleSearchRepositoryCustom {

    Page<ArticleSummaryRepositoryResponse> findArticles(SearchCondition condition, Pageable pageable);

    Page<ArticleSummaryRepositoryResponse> findHostedArticles(SearchCondition condition, Long memberId, Pageable pageable);

    Page<ArticleSummaryRepositoryResponse> findParticipantsArticles(SearchCondition condition, Long memberId, Pageable pageable);

    Page<ArticleSummaryRepositoryResponse> findLikedArticles(SearchCondition condition, Long memberId, Pageable pageable);


}

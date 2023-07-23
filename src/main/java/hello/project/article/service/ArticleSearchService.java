package hello.project.article.service;

import hello.project.article.domain.Article;
import hello.project.article.domain.search.ArticleSearchRepository;
import hello.project.article.domain.search.SearchCondition;
import hello.project.article.domain.search.dto.ArticleSummaryRepositoryResponse;
import hello.project.article.service.dto.request.ArticleSearchRequest;
import hello.project.article.service.dto.response.ArticlePageResponse;
import hello.project.article.service.dto.response.ArticleResponse;
import hello.project.article.service.dto.response.ArticleResponseAssembler;
import hello.project.article.service.dto.response.ArticleSummaryResponse;
import hello.project.favorite.domain.Favorite;
import hello.project.favorite.domain.FavoriteRepository;
import hello.project.member.service.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.BiFunction;

import static hello.project.article.exception.ArticleErrorCode.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ArticleSearchService {

    private static final int DEFAULT_PAGE_SIZE = 12;


    private final FavoriteRepository favoriteRepository;
    private final MemberValidator memberValidator;
    private final ArticleFindService articleFindService;
    private final ArticleSearchRepository articleSearchRepository;

    public ArticleResponse findArticle(Long articleId) {
        Article article = articleFindService.findByIdWithHost(articleId);
        return ArticleResponseAssembler.articleResponse(article);
    }

    public ArticleResponse findArticle(Long articleId, Long memberId) {
        memberValidator.validateExistMember(memberId);
        boolean favoriteChecked = favoriteRepository.existsByArticleIdAndMemberId(articleId, memberId);

        Article article = articleFindService.findByIdWithHost(articleId);
        return ArticleResponseAssembler.articleResponse(article, favoriteChecked);
    }

    public ArticlePageResponse findArticles(ArticleSearchRequest request) {
        SearchCondition searchCondition = request.toFindCondition();
        Pageable pageable = defaultPageable(request);
        Page<ArticleSummaryRepositoryResponse> articles = articleSearchRepository.findArticles(searchCondition, pageable);

        List<ArticleSummaryRepositoryResponse> articlesOfPage = getContents(articles);
        List<ArticleSummaryResponse> responses = ArticleResponseAssembler.articleSummaryResponses(articlesOfPage);


        return ArticleResponseAssembler.articlePageResponse(responses, articles.hasNext(), request.getPage());
    }

    public ArticlePageResponse findArticles(ArticleSearchRequest request, Long memberId) {
        return findArticlesRelatedMember(request, memberId, articleSearchRepository::findArticles);
    }

    public ArticlePageResponse findParticipatedArticles(ArticleSearchRequest request, Long memberId) {
        return findArticlesRelatedMember(request, memberId, (searchCondition, pageable) ->
                articleSearchRepository.findParticipantsArticles(searchCondition, memberId, pageable));
    }

    public ArticlePageResponse findHostedArticles(ArticleSearchRequest request, Long memberId) {
        return findArticlesRelatedMember(request, memberId, (searchCondition, pageable) ->
                articleSearchRepository.findHostedArticles(searchCondition, memberId, pageable));
    }

    public ArticlePageResponse findLikedArticles(ArticleSearchRequest request, Long memberId) {
        return findArticlesRelatedMember(request, memberId, (searchCondition, pageable) ->
                articleSearchRepository.findLikedArticles(searchCondition, memberId, pageable));
    }

    private Pageable defaultPageable(ArticleSearchRequest request) {
        return PageRequest.of(request.getPage(), DEFAULT_PAGE_SIZE);
    }

    private ArticlePageResponse findArticlesRelatedMember(
            ArticleSearchRequest request, Long memberId,
            BiFunction<SearchCondition, Pageable, Page<ArticleSummaryRepositoryResponse>> function) {
        memberValidator.validateExistMember(memberId);
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);

        SearchCondition condition = request.toFindCondition();
        Pageable pageable = defaultPageable(request);
        Page<ArticleSummaryRepositoryResponse> articles = function.apply(condition, pageable);

        List<ArticleSummaryRepositoryResponse> articlesOfPage = getContents(articles);
        List<ArticleSummaryResponse> responses = ArticleResponseAssembler.articleSummaryResponses(articlesOfPage, favorites);
        return ArticleResponseAssembler.articlePageResponse(responses, articles.hasNext(), request.getPage());
    }

    private List<ArticleSummaryRepositoryResponse> getContents(Page<ArticleSummaryRepositoryResponse> repositoryResponses) {
        return repositoryResponses.getContent();
    }

}

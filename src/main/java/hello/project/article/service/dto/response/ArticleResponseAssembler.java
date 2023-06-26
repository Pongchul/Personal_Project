package hello.project.article.service.dto.response;

import hello.project.article.domain.Article;
import hello.project.article.domain.Destination;
import hello.project.article.domain.Location;
import hello.project.article.domain.search.dto.ArticleSummaryRepositoryResponse;
import hello.project.favorite.domain.Favorite;
import hello.project.member.service.dto.response.MemberResponse;
import hello.project.member.service.dto.response.MemberResponseAssembler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleResponseAssembler {

    public static ArticleResponse articleResponse(Article article, boolean isMemberLiked) {
        return new ArticleResponse(article.getTitle(), MemberResponseAssembler.memberResponse(article.getHost()),
                article.getCapacity(), article.isFinishedRecruitment(), article.getCreatedTime(),
                locationResponse(article.getLocation()), isMemberLiked,
                destinationResponse(article.getDestination())
        );
    }

    public static ArticleResponse articleResponse(Article article) {
        return articleResponse(article, false);
    }


    public static List<ArticleSummaryResponse> articleSummaryResponses(List<ArticleSummaryRepositoryResponse> responses,
                                                                       List<Favorite> favorites) {
        return responses.stream()
                .map(response -> ArticleResponseAssembler.articleSummaryResponse(response, favorites))
                .collect(Collectors.toList());
    }

    private static ArticleSummaryResponse articleSummaryResponse(ArticleSummaryRepositoryResponse response,
                                                                      List<Favorite> favorites) {
        boolean isFavorite = anyFavoriteMatches(response, favorites);
        return articleSummaryResponse(response, isFavorite);
    }

    private static boolean anyFavoriteMatches(ArticleSummaryRepositoryResponse response,
                                              List<Favorite> favorites) {
        return favorites.stream()
                .anyMatch(favorite -> favorite.isSameArticle(response.getArticleId()));
    }


    public static List<ArticleSummaryResponse> articleSummaryResponses(List<ArticleSummaryRepositoryResponse> responses) {
        return responses.stream()
                .map(ArticleResponseAssembler::articleSummaryResponse)
                .collect(Collectors.toList());
    }

    private static ArticleSummaryResponse articleSummaryResponse(ArticleSummaryRepositoryResponse response) {
        return articleSummaryResponse(response, false);
    }

    public static ArticleIdResponse articleIdResponse(Article article) {
        return new ArticleIdResponse(article.getId());
    }

    private static ArticleSummaryResponse articleSummaryResponse(ArticleSummaryRepositoryResponse response, boolean isFavorite) {
        return new ArticleSummaryResponse(response.getArticleId(),response.getArticleTitle(),
                new MemberResponse(response.getHostId(),response.getHostName()),
                response.getCapacity(),isFinished(response),
                locationResponse(response.getLocation()), isFavorite,
                destinationResponse(response.getDestination())
        );
    }

    private static LocationResponse locationResponse(Location location) {
        return new LocationResponse(location.getAddress(), location.getBuildName(), location.getDetail());
    }

    private static DestinationResponse destinationResponse(Destination destination) {
        return new DestinationResponse(destination.getAddress(), destination.getBuildName(), destination.getDetail());
    }

    private static boolean isFinished(ArticleSummaryRepositoryResponse response) {
        return response.isClosedEarly();
    }

    public static ArticlePageResponse articlePageResponse(List<ArticleSummaryResponse> articleSummaryResponses,
                                                      boolean hasNextPage, int pageNumber) {
        return new ArticlePageResponse(hasNextPage, pageNumber, articleSummaryResponses);
    }

}

package hello.project.article.service.dto.response;

import hello.project.article.domain.Article;
import hello.project.article.domain.Destination;
import hello.project.article.domain.Location;
import hello.project.article.domain.search.dto.ArticleSummaryRepositoryResponse;
import hello.project.comm.BaseDateTime;
import hello.project.member.service.dto.response.MemberResponseAssembler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleResponseAssembler {

    public static ArticleResponse articleResponse(Article article) {
        return new ArticleResponse(article.getTitle(), MemberResponseAssembler.memberResponse(article.getHost()),
                article.getCapacity(), article.isFinishedRecruitment(), article.getCreatedTime(), locationResponse(article.getLocation()),
                destinationResponse(article.getDestination()));
    }



    private static LocationResponse locationResponse(Location location) {
        return new LocationResponse(location.getAddress(), location.getDetail());
    }

    private static DestinationResponse destinationResponse(Destination destination) {
        return new DestinationResponse(destination.getAddress(), destination.getDetail());
    }

    public static ArticleIdResponse articleIdResponse(Article article) {
        return new ArticleIdResponse(article.getId());
    }
}

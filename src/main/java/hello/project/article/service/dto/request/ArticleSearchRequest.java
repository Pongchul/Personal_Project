package hello.project.article.service.dto.request;

import hello.project.article.domain.Location;
import hello.project.article.domain.search.SearchCondition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ArticleSearchRequest {

    private int page = 0;
    private String location;
    private String destination;
    private String CurrentState;
    private String keyword;
    private boolean alreadyClosed;

    public SearchCondition toFindCondition() {
        return new SearchCondition(keyword, toFindCondition().orderByCurrentState(), toFindCondition().excludeFinished());
    }
}

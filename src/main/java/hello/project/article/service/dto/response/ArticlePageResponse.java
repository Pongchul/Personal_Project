package hello.project.article.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ArticlePageResponse {

    boolean hasNextPage;
    int pageNumber;
    List<ArticleSummaryResponse> articles;
}

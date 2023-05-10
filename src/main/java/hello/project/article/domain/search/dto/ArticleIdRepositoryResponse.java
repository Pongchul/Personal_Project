package hello.project.article.domain.search.dto;

import hello.project.comm.BaseDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleIdRepositoryResponse {
    private Long ArticleId;
    private BaseDateTime baseDateTime;
}

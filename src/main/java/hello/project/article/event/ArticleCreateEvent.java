package hello.project.article.event;

import lombok.Getter;

@Getter
public class ArticleCreateEvent {

    private final Long articleId;

    public ArticleCreateEvent(Long articleId) {
        this.articleId = articleId;
    }
}

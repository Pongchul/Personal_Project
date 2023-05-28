package hello.project.article.event;

import lombok.Getter;

@Getter
public class ArticleDeleteEvent {

    private final Long id;

    public ArticleDeleteEvent(Long id) {
        this.id = id;
    }
}

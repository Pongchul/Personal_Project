package hello.project.article.service.dto.request;

import hello.project.article.domain.Contents;
import hello.project.article.domain.CurrentState;
import hello.project.article.domain.Title;
import hello.project.article.domain.participant.Capacity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArticleRequest {

    private final String title;
    private final int capacity;
    private final String content;


    public Title getTitle() {
        return new Title(title);
    }


    public Capacity getCapacity() {
        return new Capacity(capacity);
    }

    public Contents getContents() {
        return new Contents(content);
    }

}

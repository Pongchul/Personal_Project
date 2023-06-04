package hello.project.article.domain.search;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SearchCondition {

    private String keyword;
    private boolean orderByCurrentState;
    private boolean excludeFinished;


    public String getKeyword() {
        return keyword;
    }

    public boolean orderByCurrentState() {
        return orderByCurrentState;
    }

    public boolean excludeFinished() {
        return excludeFinished;
    }
}

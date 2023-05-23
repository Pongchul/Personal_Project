package hello.project.article.domain.search;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SearchCondition {

    private String location;
    private String keyword;
    private String CurrentState;
    private boolean closedEarly;

    public String getLocation() {
        return location;
    }

    public String getKeyword() {
        return keyword;
    }

    public boolean closedEarly() {
        return closedEarly;
    }
}

package hello.project.article.domain;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum CurrentState {

    RECRUITING("모집중"),
    COMPLETED("모집완료");

    private final String currentStateName;

    public boolean isCompleted() {
        return this == COMPLETED;
    }

    public String getCurrentState() {
        return currentStateName;
    }


}

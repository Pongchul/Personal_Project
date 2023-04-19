package hello.project.article.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CurrentState {

    RECRUITING("모집중"),
    COMPLETED("모집완료");

    private final String currentState;


}

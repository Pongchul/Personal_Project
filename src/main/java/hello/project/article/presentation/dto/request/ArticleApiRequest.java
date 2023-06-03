package hello.project.article.presentation.dto.request;

import hello.project.article.domain.CurrentState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ArticleApiRequest {

    @NotNull
    private String title;

    @NotNull
    private Integer capacity;

    @NotNull
    private String contents;

    @NotNull
    private LocationApiRequest location;

    @NotNull
    private DestinationApiRequest destination;

    @NotNull
    private CurrentState currentState;



    @NotNull
    private String description;
}

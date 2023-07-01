package hello.project.article.domain.search.dto;

import hello.project.article.domain.Destination;
import hello.project.article.domain.Location;
import hello.project.article.domain.participant.Capacity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleSummaryRepositoryResponse {

    private Long articleId;
    private String articleTitle;
    private Long hostId;
    private String hostName;
    private Location location;
    private Destination destination;
    private int capacity;
    private int numOfParticipant;
    private boolean closedEarly;

}

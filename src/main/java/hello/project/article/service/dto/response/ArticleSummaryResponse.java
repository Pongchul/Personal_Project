package hello.project.article.service.dto.response;

import hello.project.member.service.dto.response.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleSummaryResponse {

    private Long id;
    private String title;
    private MemberResponse host;
    private int capacity;
    private boolean finished;
    private LocationResponse location;
    private DestinationResponse destination;
}

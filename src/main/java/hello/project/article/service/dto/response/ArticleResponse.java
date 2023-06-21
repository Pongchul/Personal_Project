package hello.project.article.service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import hello.project.comm.BaseDateTime;
import hello.project.member.service.dto.response.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ArticleResponse {

    private String title;
    private MemberResponse host;
    private int capacity;
    private boolean finished;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;
    private LocationResponse location;
    private boolean like;
    private DestinationResponse destination;


}

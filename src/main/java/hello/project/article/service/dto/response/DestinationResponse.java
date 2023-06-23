package hello.project.article.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DestinationResponse {

    private String address;
    private String buildName;
    private String detail;
}

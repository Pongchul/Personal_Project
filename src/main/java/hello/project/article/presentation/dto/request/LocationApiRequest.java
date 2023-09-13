package hello.project.article.presentation.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LocationApiRequest {

    @NotNull
    private String address;

    @NotNull
    private String buildName;
    @NotNull
    private String detail;
}

package hello.project.fixture;

import hello.project.article.domain.Destination;
import hello.project.article.presentation.dto.request.DestinationApiRequest;
import hello.project.article.service.dto.request.DestinationRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DestinationFixture {

    구로역_스타벅스("서울시 새말로 93","NC 백화점", "1층"),
    신도림역_스타벅스("서울시 새말로 211","디큐브 현대 백화점","2층"),
    롯데월드("서울시 송파 올림픽 240", "롯데월드","정문")
    ;

    private final String address;
    private final String buildingName;
    private final String detail;

    public Destination toDestination() {
        return new Destination(address, buildingName, detail);
    }

    public DestinationRequest toRequest() {
        return new DestinationRequest(address, buildingName, detail);
    }

    public DestinationApiRequest toApiRequest() {
        return new DestinationApiRequest(address, buildingName, detail);
    }
}

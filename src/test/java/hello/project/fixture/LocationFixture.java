package hello.project.fixture;

import hello.project.article.domain.Location;
import hello.project.article.presentation.dto.request.LocationApiRequest;
import hello.project.article.service.dto.request.LocationRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LocationFixture {

    잠실캠퍼스("서울 송파구 올림픽로 35다길 42", "루터회관", "13층"),
    선릉캠퍼스("서울 강남구 테헤란로 411", "성담빌딩", "14층"),
    잠실역_스타벅스("서울 송파구 올림픽로 289", "시그마타워", "1층"),
    선릉역_스타벅스("서울 강남구 테헤란로 334", "LG화재빌딩", "1층"),
    ;

    private final String address;
    private final String buildingName;
    private final String detail;

    public Location tolocation() {
        return new Location(address, buildingName, detail);
    }

    public LocationRequest toRequest() {
        return new LocationRequest(address, buildingName, detail);
    }

    public LocationApiRequest toApiRequest() {
        return new LocationApiRequest(address, buildingName, detail);
    }
}

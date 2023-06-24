package hello.project.article.service.dto.request;

import hello.project.article.domain.Location;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LocationRequest {

    private final String address;
    private final String buildName;
    private final String detail;

    public Location getLocation() {
        return new Location(address,buildName,detail);
    }
}

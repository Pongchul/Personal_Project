package hello.project.article.service.dto.request;

import hello.project.article.domain.Destination;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DestinationRequest {

    private final String address;
    private final String detail;

    public Destination getLocation() {
        return new Destination(address, detail);
    }
}

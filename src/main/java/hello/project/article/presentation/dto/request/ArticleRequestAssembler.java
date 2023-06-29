package hello.project.article.presentation.dto.request;

import hello.project.article.service.dto.request.ArticleRequest;
import hello.project.article.service.dto.request.DestinationRequest;
import hello.project.article.service.dto.request.LocationRequest;
import org.springframework.stereotype.Component;

@Component
public class ArticleRequestAssembler {

    public ArticleRequest articleRequest(ArticleApiRequest request) {
        return new ArticleRequest(request.getTitle(), request.getCapacity(), request.getContents(), request.getCurrentState(),
                locationRequest(request.getLocation()), destinationRequest(request.getDestination()));
    }

    private LocationRequest locationRequest(LocationApiRequest request) {
        return new LocationRequest(request.getAddress(),request.getBuildName(), request.getDetail());
    }

    private DestinationRequest destinationRequest(DestinationApiRequest request) {
        return new DestinationRequest(request.getAddress(), request.getBuildName(),request.getDetail());
    }
}

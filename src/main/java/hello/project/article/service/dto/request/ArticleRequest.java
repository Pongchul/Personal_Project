package hello.project.article.service.dto.request;

import hello.project.article.domain.*;
import hello.project.article.domain.participant.Capacity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArticleRequest {

    private final String title;
    private final int capacity;
    private final String content;
    private final String currentState;
    private final LocationRequest location;
    private final DestinationRequest destination;



    public Title getTitle() {
        return new Title(title);
    }

    public Capacity getCapacity() {
        return new Capacity(capacity);
    }

    public Contents getContents() {
        return new Contents(content);
    }

    public Location getLocation() {
        return location.getLocation();
    }
    public Destination getDestination() {
        return destination.getLocation();
    }

    public String getCurrentState() {
        return currentState;
    }



}

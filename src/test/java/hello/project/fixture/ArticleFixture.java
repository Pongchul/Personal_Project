package hello.project.fixture;

import hello.project.article.domain.*;
import hello.project.article.domain.participant.Capacity;
import hello.project.article.presentation.dto.request.ArticleApiRequest;
import hello.project.article.service.dto.request.ArticleRequest;
import hello.project.member.domain.Member;
import lombok.Getter;


import static hello.project.article.domain.CurrentState.*;
import static hello.project.fixture.DestinationFixture.*;
import static hello.project.fixture.LocationFixture.*;

@Getter
public enum ArticleFixture {

    TO_GANGNAM("강남으로",4,"강남 같이 갈 사람 찾습니다.", 잠실캠퍼스, 구로역_스타벅스, RECRUITING),
    TO_SINDORIM("신도림갈사람",4,"신도림 선착순 4명", 잠실역_스타벅스, 신도림역_스타벅스, RECRUITING),
    TO_LOTTEWORLD("목적지 이쪽인 사람",4,"롯데월드 오픈 할 때 같이 갈 사람",선릉역_스타벅스,롯데월드, RECRUITING)

    ;

    private final Title title;
    private final Capacity capacity;
    private final Contents contents;
    private final LocationFixture location;
    private final DestinationFixture destination;
    private final CurrentState currentState;

    ArticleFixture(String title, Integer capacity, String contents, LocationFixture location,
                   DestinationFixture destination, CurrentState currentState) {
        this.title = new Title(title);
        this.capacity = new Capacity(capacity);
        this.contents = new Contents(contents);
        this.location = location;
        this.destination = destination;
        this.currentState = currentState;
    }

    public Builder builder() {
        return new Builder(this);
    }


    public Location getLocationObject() {
        return new Location(location.getAddress(), location.getBuildingName(), location.getDetail());
    }

    public Destination getDestinationObject() {
        return new Destination(destination.getAddress(), destination.getBuildingName(), destination.getDetail());
    }
    public Article toArticle(Member host) {
        return builder().toArticle(host);
    }

    public static class Builder {

        private String title;

        private Integer capacity;

        private String contents;

        private LocationFixture location;

        private DestinationFixture destination;

        private CurrentState currentState;


        public Builder (ArticleFixture article) {
            this.title = article.getTitle().getValue();
            this.capacity = article.getCapacity().getValue();
            this.contents = article.getContents().getValue();
            this.location = article.getLocation();
            this.destination = article.getDestination();
            this.currentState = article.getCurrentState();
        }

        public Builder name(String title) {
            this.title = title;
            return this;
        }

        public Builder capacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder capacity(Capacity capacity) {
            return capacity(capacity.getValue());
        }

        public Article toArticle(Member host) {
            return new Article(host, currentState, new Capacity(capacity), new Title(title),
                    new Location(location.getAddress(),location.getBuildingName(),location.getDetail()),
                    new Destination(destination.getAddress(), destination.getBuildingName(), destination.getDetail()),
                    new Contents(contents));
        }

        public ArticleRequest toRequest() {
            return new ArticleRequest(title, capacity, contents, currentState, location.toRequest(), destination.toRequest());
        }

        public ArticleApiRequest toApiRequest() {
            return new ArticleApiRequest(title, capacity, contents, location.toApiRequest(), destination.toApiRequest(), currentState);
        }

    }

}

package hello.project.article.domain;

import hello.project.article.domain.participant.Capacity;
import hello.project.article.domain.participant.Participants;
import hello.project.article.exception.ArticleException;
import hello.project.comm.BaseDateTime;
import hello.project.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static hello.project.article.exception.ArticleErrorCode.ALREADY_CLOSED_EARLY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Participants participants;

    @Embedded
    private Contents contents;

    @Embedded
    private Destination destination;

    @Embedded
    private Location location;

    @Enumerated(EnumType.STRING)
    private CurrentState currentState;

    private boolean closedEarly;


    public Article(Long id, Title title, Contents contents, Destination destination, Location location, CurrentState currentState, Member host,
                   LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.destination = destination;
        this.location = location;
        this.currentState = currentState;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public Article(Member host,CurrentState currentState, Capacity capacity, Title title, Location location, Destination destination, Contents contents) {
        this.participants = new Participants(host, capacity);
        this.title = title;
        this.location = location;
        this.destination = destination;
        this.contents = contents;
        this.currentState = currentState;
    }

    public void update(Capacity capacity, Contents contents, Location location, Title title, CurrentState currentState) {
        validateGroupIsProceeding();
        this.participants.updateCapacity(capacity);
        this.contents = contents;
        this.location = location;
        this.title = title;
        this.currentState = currentState;
    }

    public void closeEarly() {
        validateGroupIsProceeding();
        closedEarly = true;
    }

    public void validateGroupIsProceeding() {
        validateGroupIsNotClosedEarly();
    }

    public void validateGroupIsNotClosedEarly() {
        if (closedEarly) {
            throw new ArticleException(ALREADY_CLOSED_EARLY);
        }
    }

    public void participate(Member member) {
        validateGroupIsProceeding();
        participants.participant(this, member);
    }

    public void remove(Member participant) {
        validateGroupIsProceeding();
        participants.remove(participant);
    }

    public boolean isHost(Member member) {
        return participants.isHost(member);
    }

    public boolean isNotHost(Member member) {
        return !isHost(member);
    }


    public Member getHost() {
        return participants.getHost();
    }

    public boolean isFinishedRecruitment() {
        return closedEarly;
    }

    public Location getLocation() {
        return location;
    }

    public Destination getDestination() {
        return destination;
    }

    public Contents getContents() {
        return contents;
    }

    public String getTitle() {
        return title.getValue();
    }

    public int getCapacity() {
        return participants.getCapacity().getValue();
    }

    public List<Member> getParticipants() {
        return participants.getParticipants();
    }


}

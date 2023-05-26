package hello.project.article.domain.participant;

import hello.project.article.domain.Article;
import hello.project.article.exception.ArticleException;
import hello.project.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static hello.project.article.exception.ArticleErrorCode.*;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participants {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private Member host;

    @OneToMany(mappedBy = "article", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private final List<Participant> participants = new ArrayList<>();

    @Embedded
    private Capacity capacity;

    public Participants(Member host, Capacity capacity) {
        this.host = host;
        this.capacity = capacity;
    }

    public void participant(Article article, Member member) {
        validateMemberCanParticipate(member);
        participants.add(new Participant(article, member));
    }

    private void validateMemberCanParticipate(Member member) {
        validateMemberIsNotHost(member);
        validateMemberIsNotParticipant(member);
        validateParticipantsNotYetFull();
    }

    public void remove(Member member) {
        validateMemberCanLeave(member);
        removeParticipant(member);
    }

    private void removeParticipant(Member member) {
        participants.stream()
                .filter(participant -> participant.isSameMember(member))
                .findAny()
                .ifPresent(participants::remove);
    }

    private void validateMemberCanLeave(Member member) {
        validateMemberIsNotHost(member);
        validateMemberIsParticipant(member);
    }

    private void validateMemberIsParticipant(Member member) {
        if (!contains(member)) {
            throw new ArticleException(MEMBER_IS_NOT_PARTICIPANT);
        }
    }

    public void updateCapacity(Capacity capacity) {
        validateCapacityIsOverParticipantsSize(capacity);
        this.capacity = capacity;
    }

    private void validateCapacityIsOverParticipantsSize(Capacity capacity) {
        if (capacity.isSmallThan(getSize())) {
            throw new ArticleException(CAPACITY_CANNOT_BE_LESS_THAN_PARTICIPANTS_SIZE);
        }
    }

    private void validateParticipantsNotYetFull() {
        if (isFull()) {
            throw new ArticleException(ALREADY_PARTICIPANTS_SIZE_FULL);
        }
    }

    private boolean isFull() {
        return capacity.isEqualOrOver(getSize());
    }

    private int getSize() {
        return getParticipants().size();
    }

    private void validateMemberIsNotParticipant(Member member) {
        if (contains(member)) {
            throw new ArticleException(MEMBER_IS_PARTICIPANT);
        }
    }

    private void validateMemberIsNotHost(Member member) {
        if (host.equals(member)) {
            throw new ArticleException(MEMBER_IS_HOST);
        }
    }


    private boolean contains(Member member) {
        return getParticipants().contains(member);
    }


    public boolean isHost(Member member) {
        return host.isSameUserId(member);
    }

    public List<Member> getParticipants() {
        List<Member> members = participants.stream()
                .map(Participant::getMember)
                .collect(Collectors.toUnmodifiableList());

        return Stream.of(List.of(host), members)
                .flatMap(Collection::stream)
                .collect(Collectors.toUnmodifiableList());
    }
}

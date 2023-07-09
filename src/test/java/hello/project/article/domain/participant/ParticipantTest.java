package hello.project.article.domain.participant;

import hello.project.article.domain.Article;
import hello.project.article.exception.ArticleException;
import hello.project.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static hello.project.fixture.ArticleFixture.TO_GANGNAM;
import static hello.project.fixture.MemberFixture.INCHUL;
import static hello.project.fixture.MemberFixture.PONGCHUL;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ParticipantTest {

    private static final Member HOST = PONGCHUL.toMember();
    private static final Member PARTICIPANT = INCHUL.toMember();
    private static final Article ARTICLE = TO_GANGNAM.toArticle(HOST);
    private static final Capacity CAPACITY = new Capacity(4);


    @DisplayName("정상적으로 생성한다")
    @Test
    void construct() {
        Participants participants = new Participants(HOST, CAPACITY);

        assertAll(
                () -> assertThat(participants.getHost()).isEqualTo(HOST),
                () -> assertThat(participants.getParticipants()).containsExactly(HOST),
                () -> assertThat(participants.getCapacity()).isEqualTo(CAPACITY)
        );
    }

    @DisplayName("참여한다")
    @Test
    void participate() {
        Participants participants = new Participants(HOST, CAPACITY);
        participants.participant(ARTICLE, PARTICIPANT);

        assertThat(participants.getParticipants()).containsExactly(HOST, PARTICIPANT);
    }

    @DisplayName("주최자가 모임에 참여할 경우 예외가 발생한다")
    @Test
    void validateMemberIsNotHostWhenParticipate() {
        Participants participants = new Participants(HOST, CAPACITY);

        assertThatThrownBy(() -> participants.participant(ARTICLE, HOST))
                .isInstanceOf(ArticleException.class)
                .hasMessage("MEMBER_IS_HOST");
    }

    @DisplayName("참여자가 다시 참여할 경우 예외가 발생한다")
    @Test
    void validateMemberIsNotParticipatedWhenParticipate() {
        Participants participants = new Participants(HOST, CAPACITY);
        participants.participant(ARTICLE, PARTICIPANT);

        assertThatThrownBy(() -> participants.participant(ARTICLE, HOST))
                .isInstanceOf(ArticleException.class)
                .hasMessage("MEMBER_IS_PARTICIPANT");
    }

}
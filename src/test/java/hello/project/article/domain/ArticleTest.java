package hello.project.article.domain;

import hello.project.article.domain.participant.Capacity;
import hello.project.article.exception.ArticleException;
import hello.project.fixture.ArticleFixture;
import hello.project.member.domain.Member;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

import static hello.project.article.domain.CurrentState.RECRUITING;
import static hello.project.fixture.ArticleFixture.TO_GANGNAM;
import static hello.project.fixture.ArticleFixture.TO_SINDORIM;
import static hello.project.fixture.MemberFixture.INCHUL;
import static hello.project.fixture.MemberFixture.PONGCHUL;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {

    private static final Member HOST = INCHUL.toMember();

    @DisplayName("조기마감된 모임에 대한 검증 테스트")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class EarlyClosedArticleTest {

        private Article article;

        @BeforeEach
        void setUp() {
            article = TO_SINDORIM.toArticle(INCHUL.toMember());
            article.closeEarly();
        }

        @DisplayName("조기마감된 모임은 더이상 수정할 수 없습니다")
        @Test
        void cannotUpdateArticleByClosedEarly() {
            assertThatThrownBy(() -> update(article, TO_GANGNAM))
                    .isInstanceOf(ArticleException.class)
                    .hasMessage("ALREADY_CLOSED_EARLY");
        }

        @DisplayName("조기마감된 모임은 더이상 삭제할 수 없습니다")
        @Test
        void cannotDeleteGroupByClosedEarly() {
            assertThatThrownBy(article::validateGroupIsProceeding)
                    .isInstanceOf(ArticleException.class)
                    .hasMessage("ALREADY_CLOSED_EARLY");
        }

        @DisplayName("이미 조기마감된 모임은 다시 조기마감할 수 없습니다.")
        @Test
        void cannotClosedArticleByAlreadyClosed() {
            assertThatThrownBy(article::validateGroupIsProceeding)
                    .isInstanceOf(ArticleException.class)
                    .hasMessage("ALREADY_CLOSED_EARLY");
        }

        @DisplayName("이미 조기마감된 모임은 참여할 수 없습니다.")
        @Test
        void cannotParticipateByAlreadyClosed() {
            Member participant = INCHUL.toMember();

            assertThatThrownBy(() -> article.participate(participant))
                    .isInstanceOf(ArticleException.class)
                    .hasMessage("ALREADY_CLOSED_EARLY");
        }

        @DisplayName("이미 조기마감된 모임을 탈퇴한다")
        @Test
        void cannotLeaveByAlreadyClosed() {
            Member participant = INCHUL.toMember();

            assertThatThrownBy(() -> article.remove(participant))
                    .isInstanceOf(ArticleException.class)
                    .hasMessage("ALREADY_CLOSED_EARLY");
        }

    }

    @DisplayName("참여자가 존재하는 모임에 대한 검증")
    @Nested
    class ParticipantsExistArticleTest {

        private Article article;

        @BeforeEach
        void setUp() {
            article = TO_GANGNAM.toArticle(INCHUL.toMember());
            article.participate(PONGCHUL.toMember());
        }

        @DisplayName("참여자가 존재하는 모임을 수정할 수 있습니다")
        @Test
        void cannotUpdateGroupByExistParticipants() {
            assertDoesNotThrow(() -> update(article, TO_GANGNAM));
        }

        @DisplayName("참여자가 존재하는 모임을 삭제할 수 있습니다")
        @Test
        void cannotDeleteGroupByExistParticipants() {
            assertDoesNotThrow(article::validateGroupIsProceeding);

        }
    }


    @DisplayName("모임을 수정한다")
    @Test
    void update() {
        Member host = INCHUL.toMember();
        Article article = TO_SINDORIM.toArticle(host);

        ArticleFixture fixture = TO_GANGNAM;
        Capacity capacity = fixture.getCapacity();
        Contents contents = fixture.getContents();
        Title title = fixture.getTitle();
        Location location = fixture.getLocationObject();
        Destination destination = fixture.getDestinationObject();
        CurrentState currentState = fixture.getCurrentState();

        article.update(capacity, contents, location, title, destination, currentState);

        assertAll(
                () -> assertThat(article.getHost().getUserId()).isEqualTo(host.getUserId()),
                () -> assertThat(article.getCapacity()).isEqualTo(capacity.getValue()),
                () -> assertThat(article.getTitle()).isEqualTo(title.getValue()),
                () -> assertThat(article.getCurrentState()).isEqualTo(RECRUITING),
                () -> assertThat(article.getLocation()).isEqualTo(location),
                () -> assertThat(article.getDestination()).isEqualTo(destination)
        );
    }

    private void update(Article article, ArticleFixture fixture) {
        Capacity capacity = fixture.getCapacity();
        Contents contents = fixture.getContents();
        Title title = fixture.getTitle();
        Location location = fixture.getLocationObject();
        Destination destination = fixture.getDestinationObject();
        CurrentState currentState = fixture.getCurrentState();

        article.update(capacity, contents, location, title, destination, currentState);
    }

    @DisplayName("모임 모집을 조기마감한다")
    @Test
    void closeEarly() {
        Article article = TO_GANGNAM.toArticle(INCHUL.toMember());
        article.closeEarly();

        assertThat(article.isClosedEarly()).isTrue();
    }

    @DisplayName("모임에 참여한다")
    @Test
    void participate() {
        Article article = TO_GANGNAM.toArticle(INCHUL.toMember());
        Member participant = PONGCHUL.toMember();
        article.participate(participant);

        assertThat(article.getParticipants()).usingRecursiveComparison()
                .isEqualTo(List.of(HOST, participant));
    }

    @DisplayName("모임을 탈퇴한다")
    @Test
    void remove() {
        Article article = TO_GANGNAM.toArticle(INCHUL.toMember());
        Member participant = PONGCHUL.toMember();
        article.participate(participant);
        article.remove(participant);

        assertThat(article.getParticipants()).usingRecursiveComparison()
                .isEqualTo(List.of(HOST));
    }

    @DisplayName("모임의 주최자와 일치하는지 확인한다")
    @ParameterizedTest
    @MethodSource("provideIsHostArguments")
    void isHost(Member member, boolean expected) {
        Article article = TO_GANGNAM.toArticle(INCHUL.toMember());

        assertThat(article.isHost(member)).isEqualTo(expected);
    }

    private static Stream<Arguments> provideIsHostArguments() {
        return Stream.of(
                Arguments.of(HOST, true),
                Arguments.of(PONGCHUL.toMember(), false)
        );
    }

    @DisplayName("마감되지 않은 모임에 대해, 모집이 마감되었는지 확인한다")
    @Test
    void isFinishedRecruitment() {
        Article article = TO_GANGNAM.toArticle(INCHUL.toMember());

        assertThat(article.isFinishedRecruitment()).isFalse();
    }

    @DisplayName("조기마감된 모임에 대해, 모집이 마감되었는지 확인한다")
    @Test
    void isFinishedRecruitmentWhenClosedEarly() {

        Article article = TO_GANGNAM.toArticle(INCHUL.toMember());
        article.closeEarly();

        assertThat(article.isFinishedRecruitment()).isTrue();
    }
}


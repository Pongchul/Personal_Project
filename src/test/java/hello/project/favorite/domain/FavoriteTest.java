package hello.project.favorite.domain;

import hello.project.article.domain.Article;
import hello.project.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static hello.project.fixture.ArticleFixture.TO_GANGNAM;
import static hello.project.fixture.ArticleFixture.TO_SINDORIM;
import static hello.project.fixture.MemberFixture.PONGCHUL;
import static org.assertj.core.api.Assertions.*;

class FavoriteTest {

    private Long memberId;
    private Long toGangNamArticleId;
    private Long toSimDoRimArticleId;


    @BeforeEach
    void setUp() {
        Member member = PONGCHUL.toMember();
        setMemberId(member, 1L);
        memberId = member.getId();

        Article gangNamArticle = TO_GANGNAM.toArticle(member);
        setArticleId(gangNamArticle, 1L);
        toGangNamArticleId = gangNamArticle.getId();

        Article simDoRimArticle = TO_SINDORIM.toArticle(member);
        setArticleId(simDoRimArticle, 2L);
        toSimDoRimArticleId = simDoRimArticle.getId();
    }

    @DisplayName("동일함 게시글일 경우 True를 반환한다.")
    @Test
    void isSameArticle() {
        Favorite favorite = new Favorite(toGangNamArticleId, memberId);
        boolean actual = favorite.isSameArticle(toSimDoRimArticleId);

        assertThat(actual).isTrue();
    }

    @DisplayName("동일한 게시글이 아닐 경우 False를 반환한다")
    @Test
    void isNotSameArticle() {
        Favorite favorite = new Favorite(toGangNamArticleId, memberId);
        boolean actual = favorite.isSameArticle(toSimDoRimArticleId);

        assertThat(actual).isFalse();
    }

    void setArticleId(Article article, Long articleId) {
        try {
            Field fieldId = Article.class.getDeclaredField("id");
            fieldId.setAccessible(true);
            fieldId.set(article, articleId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("해당하는 필드를 찾을 수 없습니다.");
        }
    }

    void setMemberId(Member member, Long memberId) {
        try {
            Field fieldId = Member.class.getDeclaredField("id");
            fieldId.setAccessible(true);
            fieldId.set(member, memberId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("해당하는 필드를 찾을 수 없습니다.");
        }
    }
}
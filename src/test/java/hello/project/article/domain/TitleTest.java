package hello.project.article.domain;

import hello.project.article.exception.ArticleException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class TitleTest {

    @DisplayName("게시글 이름이 조건에 부합하면 생성한다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 50})
    void construct(int nameLength) {
        String name = "a".repeat(nameLength);
        Title title = new Title(name);
        assertThat(title.getValue()).isEqualTo(name);
    }

    @DisplayName("게시글 이름이 빈값이면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {1, 50})
    void nameMustNotBlank(int nameLength) {
        String name = " ".repeat(nameLength);
        assertThatThrownBy(() -> new Title(name))
                .isInstanceOf(ArticleException.class)
                .hasMessage("TITLE_CANNOT_BE_BLANK");
    }

    @DisplayName("게시글 이름 길이가 정책 범위를 벗어나면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {51})
    void nameLengthOutOfRangeException(int naneLength) {
        String name = "a".repeat(naneLength);
        assertThatThrownBy(() -> new Title(name))
                .isInstanceOf(ArticleException.class)
                .hasMessage("TITLE_CANNOT_BE_OUT_OF_RANGE");
    }

}
package hello.project.article.domain;

import hello.project.article.exception.ArticleException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ContentsTest {

    @DisplayName("게시글 설명이 조건에 부합하면 생성한다")
    @ParameterizedTest
    @ValueSource(ints = {1000})
    void construct(int length) {
        String expected = "a".repeat(length);
        Contents actual = new Contents(expected);

        assertThat(actual.getValue()).isEqualTo(expected);
    }

    @DisplayName("게시글 설명 길이가 정책 범위를 벗어나면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {1001})
    void nameLengthOutOfRangeException(int length) {
        String expected = "a".repeat(length);
        assertThatThrownBy(() -> new Contents(expected))
                .isInstanceOf(ArticleException.class)
                .hasMessage("CONTENTS_CANNOT_BE_OUT_OF_RANGE");
    }
}
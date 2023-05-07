package hello.project.article.domain;

import hello.project.article.exception.ArticleException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static hello.project.article.exception.ArticleErrorCode.TITLE_CANNOT_BE_BLANK;
import static hello.project.article.exception.ArticleErrorCode.TITLE_CANNOT_BE_OUT_OF_RANGE;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Title {

    private static final int MINIMUM_LENGTH = 1;
    private static final int MAXIMUM_LENGTH = 50;

    @Column(name = "title", nullable = false, length = 50)
    private String value;

    public Title(String value) {
        validateNotBlank(value);
        validateLengthInRange(value);
        this.value = value;
    }

    private void validateLengthInRange(String value) {
        if (value.isBlank()) {
            throw new ArticleException(TITLE_CANNOT_BE_BLANK);
        }
    }

    private void validateNotBlank(String value) {
        if (isTitleOutOfRange(value)) {
            throw new ArticleException(TITLE_CANNOT_BE_OUT_OF_RANGE);
        }
    }

    private boolean isTitleOutOfRange(String value) {
        int length = value.length();
        return (length < MINIMUM_LENGTH) || (MAXIMUM_LENGTH < length);
    }
}

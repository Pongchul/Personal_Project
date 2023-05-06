package hello.project.article.domain;

import hello.project.article.exception.ArticleErrorCode;
import hello.project.article.exception.ArticleException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@ToString(includeFieldNames = false)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Contents {

    private static final int MAXIMUM_LENGTH = 1000;

    @Lob
    @Column(name = "contents", nullable = false)
    private String value;

    public Contents(String value) {
        validateLengthInRange(value);
        this.value = value;
    }

    private void validateLengthInRange(String value) {
        if (MAXIMUM_LENGTH < value.length()) {
            throw new ArticleException(ArticleErrorCode.CONTENTS_CANNOT_BE_OUT_OF_RANGE);
        }
    }
}

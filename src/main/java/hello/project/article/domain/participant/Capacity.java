package hello.project.article.domain.participant;

import hello.project.article.exception.ArticleException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static hello.project.article.exception.ArticleErrorCode.CAPACITY_CANNOT_BE_OUT_OF_RANGE;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Capacity {

    private static final int MINIMUM = 1;
    private static final int MAXIMUM = 5;

    @Column(name = "capacity", nullable = false)
    private int value;

    public Capacity(int value) {
        validateCapacityIsInRange(value);
        this.value = value;
    }

    public boolean isEqualOrOver(int numberOfPeople) {
        return value <= numberOfPeople;
    }

    public boolean isSmallThan(int numberOfPeople) {
        return value < numberOfPeople;
    }

    private void validateCapacityIsInRange(int value) {
        if (isOutOfRange(value)) {
            throw new ArticleException(CAPACITY_CANNOT_BE_OUT_OF_RANGE);
        }
    }

    private static boolean isOutOfRange(int capacity) {
        return MINIMUM > capacity || capacity > MAXIMUM;
    }
}

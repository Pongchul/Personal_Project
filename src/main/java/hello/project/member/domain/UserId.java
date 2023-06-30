package hello.project.member.domain;

import hello.project.member.exception.MemberErrorCode;
import hello.project.member.exception.MemberException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static hello.project.member.exception.MemberErrorCode.*;

@ToString(includeFieldNames = false)
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserId {

    private static final int MINIMUM_LENGTH = 4;
    private static final int MAXIMUM_LENGTH = 50;
    private static final String EMAIL_FORMAT = "@";

    @Column(name = "user_id", unique = true, length=50)
    private String value;

    private UserId(String value) {
        this.value = value;
    }

    public static UserId userId(String value) {
        validateNotBlank(value);
        validateLengthInRange(value);
        validateNotEmailPattern(value);
        return new UserId(value);
    }

    public static UserId oauth(String value) {
        validateEmailPattern(value);
        validateLengthInRange(value);
        return new UserId(value);
    }

    private static void validateNotBlank(String value) {
        if (value.isBlank()) {
            throw new MemberException(USER_ID_SHOULD_NOT_BE_BLANK);
        }
    }

    private static void validateLengthInRange(String userId) {
        int length = userId.length();
        if (length < MINIMUM_LENGTH || MAXIMUM_LENGTH < length) {
            throw new MemberException(USER_ID_CANNOT_BE_OUT_OF_RANGE);
        }
    }

    private static void validateNotEmailPattern(String value) {
        if (value.contains(EMAIL_FORMAT)) {
            throw new MemberException(SIGNUP_INVALID_ID);
        }
    }

    private static void validateEmailPattern(String value) {
        if (!value.contains(EMAIL_FORMAT)) {
            throw new MemberException(USER_ID_SHOULD_BE_EMAIL_FORMAT);
        }
    }

}

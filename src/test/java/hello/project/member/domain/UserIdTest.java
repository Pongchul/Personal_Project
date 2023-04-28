package hello.project.member.domain;

import hello.project.member.exception.MemberException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class UserIdTest {

    @DisplayName("사용자 아이디가 조건에 부합하면 생성한다")
    @ParameterizedTest
    @ValueSource(ints = {4, 50})
    void construct(int length) {
        String id = "a".repeat(length);
        UserId userId = UserId.userId(id);
        assertThat(userId.getValue()).isEqualTo(id);
    }

    @DisplayName("사용자 아이디가 Null이 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 3, 51, 52})
    void nullIdException(int length) {
        String id = " ".repeat(length);
        assertThatThrownBy(() -> UserId.userId(id))
                .isInstanceOf(MemberException.class)
                .hasMessage("USER_ID_SHOULD_NOT_BE_BLANK");
    }

    @DisplayName("사용자의 아이디가 규격에 안맞으면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {1, 3, 51, 52})
    void lengthOutOfRangeException(int length) {
        String id = "a".repeat(length);
        assertThatThrownBy(() -> UserId.userId(id))
                .isInstanceOf(MemberException.class)
                .hasMessage("USER_ID_CANNOT_BE_OUT_OF_RANGE");
    }

    @DisplayName("사용자의 아이디가 이메일 형식일 경우 예외가 발생한다")
    @Test
    void idMustNotBeInEmail() {
        assertThatThrownBy(() -> UserId.userId("id@naver.com"))
                .isInstanceOf(MemberException.class)
                .hasMessage("SIGNUP_INVALID_ID");
    }
}
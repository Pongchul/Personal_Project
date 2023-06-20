package hello.project.member.domain;

import hello.project.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class UserNameTest {

    @DisplayName("사용자 이름이 조건에 부합하면 생성한다")
    @ParameterizedTest
    @ValueSource(ints = {1, 20})
    void construct(int length) {
        String expected = "a".repeat(length);
        UserName userName = UserName.from(expected);

        assertThat(userName.getValue()).isEqualTo(expected);
    }

    @DisplayName("사용자의 이름이 빈값이면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {1, 20})
    void nameMustNotBlank(int length) {
        String expected = " ".repeat(length);

        assertThatThrownBy(() -> UserName.from(expected))
                .isInstanceOf(MemberException.class)
                .hasMessage("MEMBER_NAME_SHOULD_NOT_BE_BLANK");
    }

    @DisplayName("사용자의 이름이 길이 정책을 벗어나면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {21})
    void lengthOutRangeException(int length) {
        String name = "a".repeat(length);
        assertThatThrownBy(() -> UserName.from(name))
                .isInstanceOf(MemberException.class)
                .hasMessage("MEMBER_NAME_CANNOT_BE_OUT_OF_RANGE");
    }

}
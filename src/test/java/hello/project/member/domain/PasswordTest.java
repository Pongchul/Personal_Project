package hello.project.member.domain;

import hello.project.auth.support.PasswordEncoder;
import hello.project.auth.support.SHA256Encoder;
import hello.project.member.exception.MemberException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class PasswordTest {

    private final PasswordEncoder Encoding = new SHA256Encoder();

    @DisplayName("회원님의 비밀번는 규격에 맞는 패턴이어야 합니다.")
    @ParameterizedTest
    @ValueSource(strings = {" ", "1234567!", "asdfghj!", "asdf1234", "12345678", "asdfghjk", "!@#$%^&*", "a1!", "123456789asdfgh!@#$"})
    void passwordMustBeValidPattern(String password) {
        assertThatThrownBy(() -> Password.encrypt(password, Encoding))
                .isInstanceOf(MemberException.class)
                .hasMessage("MEMBER_PASSWORD_PATTERN_MUST_BE_VALID");
    }
}

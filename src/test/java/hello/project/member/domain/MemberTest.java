package hello.project.member.domain;


import hello.project.auth.support.SHA256Encoder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    private static final UserId USER_ID = UserId.userId("pongchul");
    private static final Password PASSWORD = Password.encrypt("pongchul1!", new SHA256Encoder());

    @Test
    @DisplayName("멤버 생성하기")
    void createMember() {

        Member member = new Member(USER_ID, PASSWORD);
        assertDoesNotThrow(() -> member);
    }

}
package hello.project.fixture;

import hello.project.auth.support.SHA256Encoder;
import hello.project.member.domain.Member;
import hello.project.member.domain.Password;
import hello.project.member.domain.UserId;
import lombok.Getter;


@Getter
public enum MemberFixture {

    PONGCHUL("pongchul","pongchul1!"),
    INCHUL("INCHUL","inchuhl1!")

    ;

    private final String userId;
    private final String password;

    MemberFixture(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public Member toMember() {
        return new Member(UserId.userId(userId), Password.encrypt(password, new SHA256Encoder()));
    }
}

package hello.project.fixture;

import hello.project.auth.support.SHA256Encoder;
import hello.project.member.domain.Member;
import hello.project.member.domain.Password;
import hello.project.member.domain.UserId;
import hello.project.member.domain.UserName;
import lombok.Getter;


@Getter
public enum MemberFixture {

    PONGCHUL("pongchul","pongchul1!","퐁철"),
    INCHUL("INCHUL","inchuhl1!","인처리"),
    PHOSPHOROUS("pfe11","pfe1!","인과철")

    ;

    private final String userId;
    private final String password;
    private final String userName;

    MemberFixture(String userId, String password, String userName) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
    }

    public Member toMember() {
        return new Member(UserId.userId(userId), Password.encrypt(password, new SHA256Encoder()), UserName.from("인철"));
    }
}

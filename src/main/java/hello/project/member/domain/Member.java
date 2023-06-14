package hello.project.member.domain;

import hello.project.auth.support.PasswordEncoder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    @Embedded
    private UserId userId;

    @Embedded
    private UserName userName;

    @Embedded
    private Password password;

    @Column(nullable = false)
    private boolean deleted;


    public Member(UserId userId, Password password) {
        this.userId = userId;
        this.password = password;
    }

    public boolean isNotSamePassword(String password) {
        return !this.password.isSame(password);
    }

    public void changePassword(String password, PasswordEncoder encoder) {
        this.password = this.password.update(password, encoder);
    }

    public void changeUserName(String userName) {
        this.userName = this.userName.update(userName);
    }

    public void delete() {
        userId = null;
        password = null;
        userName = null;
        deleted = true;
    }

    public boolean isSameUserId(Member member) {
        return userId.getValue().equals(member.userId.getValue());
    }

    public String getUserId() {
        return Optional.ofNullable(userId)
                .map(UserId::getValue)
                .orElse("");
    }

    public String getPassword() {
        return Optional.ofNullable(password)
                .map(Password::getValue)
                .orElse("");
    }

    public String getUserName() {
        return Optional.ofNullable(userName)
                .map(UserName::getValue)
                .orElse("");
    }
}

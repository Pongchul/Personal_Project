package hello.project.member.service;

import hello.project.auth.support.SHA256Encoder;
import hello.project.member.domain.*;
import hello.project.member.exception.MemberErrorCode;
import hello.project.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static hello.project.member.exception.MemberErrorCode.MEMBER_NOT_EXIST;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MemberFindServiceTest {

    @Autowired
    private MemberFindService memberFindService;

    @Autowired
    private MemberRepository memberRepository;

    private static final UserId USER_ID = UserId.userId("pongchul");
    private static final Password PASSWORD = Password.encrypt("pongchul1!", new SHA256Encoder());
    private static final UserName USER_NAME = UserName.from("인철");


    @DisplayName("회원을 조회한다.")
    @Test
    void findMember() {
        Member expected = memberRepository.save(new Member(USER_ID, PASSWORD,USER_NAME));

        Member actual = memberFindService.findMember(expected.getId());

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @DisplayName("존재하지 않는 회원을 조회하는 경우 예외가 발생한다.")
    @Test
    void findNotExistMember() {
        assertThatThrownBy(() -> memberFindService.findMember(1000L))
                .isInstanceOf(MemberException.class)
                .hasMessage("MEMBER_NOT_EXIST");
    }

    @DisplayName("삭제된 회원을 조회하는 경우 예외가 발생한다")
    @Test
    void findDeletedMember() {
        Member member = memberRepository.save(new Member(USER_ID, PASSWORD, USER_NAME));
        member.delete();

        assertThatThrownBy(() -> memberFindService.findMember(member.getId()))
                .isInstanceOf(MemberException.class)
                .hasMessage("MEMBER_DELETED");
    }

    @DisplayName("아이디와 비밀번호로 회원을 조회한다")
    @Test
    void findMemberByIdAndPassword() {
        Member member = memberRepository.save(new Member(USER_ID, PASSWORD, USER_NAME));

        Member foundMember = memberFindService.findByUserIdAndPassword(USER_ID, PASSWORD);
        assertThat(foundMember).usingRecursiveComparison()
                .isEqualTo(member);
    }

    @DisplayName("잘못된 아이디와 비밀번호로 회원을 조회하는 경우 예외가 발생한다")
    @Test
    void findMemberByIdAndWrongPassword() {
        Member member = memberRepository.save(new Member(USER_ID, PASSWORD, USER_NAME));

        Password wrongPassword = Password.encrypt("wrong123!", new SHA256Encoder());
        assertThatThrownBy(
                () -> memberFindService.findByUserIdAndPassword(USER_ID, wrongPassword)
        ).isInstanceOf(MemberException.class)
                .hasMessageContaining("MEMBER_INVALID_ID_AND_PASSWORD");
    }
}
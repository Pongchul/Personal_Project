package hello.project.member.service;

import hello.project.member.domain.Member;
import hello.project.member.domain.MemberRepository;
import hello.project.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import static hello.project.fixture.MemberFixture.PONGCHUL;
import static hello.project.member.exception.MemberErrorCode.MEMBER_NOT_EXIST;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@SpringBootTest
class MemberValidatorTest {

    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;

    @DisplayName("존재하지 않는 회원 id가 아니면 예외를 발생시킨다")
    @Test
    void validateExistMember() {
        Long notExistId = 100L;
        assertThatThrownBy(() -> memberValidator.validateExistMember(notExistId))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_EXIST.getMessage());
    }

    @DisplayName("삭제된 않는 회원 id가 아니면 예외를 발생시킨다")
    @Test
    void validateDeletedMember() {
        Member member = memberRepository.save(PONGCHUL.toMember());
        member.delete();

        assertThatThrownBy(() -> memberValidator.validateExistMember(member.getId()))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_EXIST.getMessage());
    }

}
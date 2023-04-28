package hello.project.member.domain;

import hello.project.auth.support.SHA256Encoder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(classes = Repository.class))
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    private static final UserId USER_ID = UserId.userId("pongchul");
    private static final UserName USER_NAME = UserName.from("퐁철");
    private static final Password PASSWORD = Password.encrypt("pongchul1!", new SHA256Encoder());

    @DisplayName("회원을 저장한다.")
    @Test
    void save() {
        Member member = new Member(USER_ID, PASSWORD);
        Long id = memberRepository.save(member).getId();

        assertThat(id).isNotNull();
    }

    @DisplayName("식별자를 통해 회원을 조회한다.")
    @Test
    void findById() {

        Member member = new Member(USER_ID, PASSWORD);
        Member savedMember = memberRepository.save(member);


        Optional<Member> savedMemberGetId = memberRepository.findById(savedMember.getId());

        assertThat(savedMemberGetId).isPresent();
        assertThat(savedMemberGetId.get()).usingRecursiveComparison().isEqualTo(savedMember);
        // usingRecursiveComparison 동일성이 아닌 동등성 검사로 쓰는 메소드
    }

    @DisplayName("USER_ID, PASSWORD가 일치하는 회원 조회한다.")
    @Test
    void findByUserIdAndPassword() {
        Member member = new Member(USER_ID, PASSWORD);
        Member savedMember = memberRepository.save(member);

        Optional<Member> byUserIdAndPassword = memberRepository.findByUserIdAndPassword(USER_ID, PASSWORD);

        assertThat(byUserIdAndPassword).isPresent();
        assertThat(byUserIdAndPassword.get()).usingRecursiveComparison().isEqualTo(savedMember);

    }
}
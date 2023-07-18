package hello.project.member.service;

import hello.project.article.domain.Article;
import hello.project.article.domain.ArticleRepository;
import hello.project.article.service.ArticleFindService;
import hello.project.auth.domain.TokenRepository;
import hello.project.auth.support.PasswordEncoder;
import hello.project.auth.support.SHA256Encoder;
import hello.project.member.domain.*;
import hello.project.member.exception.MemberException;
import hello.project.member.service.dto.request.ChangeNameRequest;
import hello.project.member.service.dto.request.ChangePasswordRequest;
import hello.project.member.service.dto.request.SignUpRequest;
import hello.project.member.service.dto.response.MyInfoResponse;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static hello.project.fixture.ArticleFixture.TO_GANGNAM;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@SpringBootTest
class MemberServiceTest {

    private final MemberService memberService;
    private final MemberFindService memberFindService;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final ArticleFindService articleFindService;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;

    private Password password;
    private Member savedHost;

    @BeforeEach
    void setUp() {
        password = Password.encrypt("pongchul1!", new SHA256Encoder());
        savedHost = memberRepository.save(new Member(UserId.userId("게시글 장"), password, UserName.from("inchul")));
    }

    @Nested // 여러 테스트 그룹간의 관계를 표현할 수 있고 계층적으로 나타낼 수 있다.
    class SignUpTest {

        private static final String USER_ID = "Phosphorus";
        private static final String PASSWORD = "Phosphorus1!";
        private static final String USER_NAME = "인철";



        @DisplayName("회원 가입을 한다")
        @Test
        void signUp() {
            SignUpRequest request = new SignUpRequest(USER_ID, PASSWORD, USER_NAME);
            Long id = memberService.signUp(request);

            assertThat(id).isNotNull();
        }

        @DisplayName("이미 존재하는 아이디로 회원 가입을 하는 경우 실패한다")
        @Test
        void signUpDuplicatedUserid() {
            SignUpRequest request = new SignUpRequest(USER_ID, PASSWORD, USER_NAME);
            memberService.signUp(request);

            SignUpRequest newRequest = new SignUpRequest(USER_ID, PASSWORD, USER_NAME);
            assertThatThrownBy(() -> memberService.signUp(newRequest))
                    .isInstanceOf(MemberException.class)
                    .hasMessageContaining("SIGNUP_USER_ID_DUPLICATED");
        }

        @DisplayName("이미 존재하는 이름으로 회원 가입을 하는 경우 실패한다")
        @Test
        void signUpDuplicatedName() {
            SignUpRequest request = new SignUpRequest("phosphorus", "phosphorus1!", "인철");
            memberService.signUp(request);

            SignUpRequest newRequest = new SignUpRequest("new" + USER_ID, PASSWORD, USER_NAME);
            assertThatThrownBy(() -> memberService.signUp(newRequest))
                    .isInstanceOf(MemberException.class)
                    .hasMessageContaining("SIGNUP_USER_NAME_DUPLICATED");
        }

    }

    @DisplayName("회원 정보를 조회한다")
    @Test
    void findById() {
        SignUpRequest request = new SignUpRequest("phosphorus", "phosphorus1!", "인철");
        Long memberId = memberService.signUp(request);

        MyInfoResponse response = memberService.findById(memberId);

        assertAll(
                () -> assertThat(response.getId()).isEqualTo(memberId),
                () -> assertThat(response.getUserId()).isEqualTo(request.getUserId())
        );
    }

    @DisplayName("존재하지 않는 회원 정보를 조회하는 경우 예외가 발생한다.")
    @Test
    void findByIdNotExist() {
        assertThatThrownBy(() -> memberService.findById(1000L))
                .isInstanceOf(MemberException.class)
                .hasMessage("MEMBER_NOT_EXIST");
    }

    @DisplayName("존재하지 않는 회원의 이름을 수정하는 경우 예외가 발생한다")
    @Test
    void updateNameNotExist() {
        ChangeNameRequest request = new ChangeNameRequest("무무");

        assertThatThrownBy(() -> memberService.updateName(1000L, request))
                .isInstanceOf(MemberException.class)
                .hasMessageContaining("MEMBER_NOT_EXIST");
    }

    @DisplayName("비밀번호를 업데이트한다")
    @Test
    void updatePassword() {
        String beforePassword = "password1!!";
        SignUpRequest signUpRequest = new SignUpRequest("pongchul", beforePassword, "인철");
        Long memberId = memberService.signUp(signUpRequest);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("newPassword123!", beforePassword);
        memberService.updatePassword(memberId, changePasswordRequest);

        Member member = memberFindService.findMember(memberId);

        String encryptedPassword = passwordEncoder.encrypt(beforePassword);
        assertThat(member.getPassword()).isNotEqualTo(encryptedPassword);
    }

    @DisplayName("잘못된 현재 비밀번호로 비밀번호를 업데이트시 예외가 발생한다")
    @Test
    void updatePasswordWithWrongPassword() {
        String password = "pongchul1!";
        SignUpRequest request = new SignUpRequest("pongchul", password, "인철");
        Long memberId = memberService.signUp(request);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("newPassword123!", "wrongPassword");
        assertThatThrownBy(() -> memberService.updatePassword(memberId, changePasswordRequest))
                .isInstanceOf(MemberException.class)
                .hasMessage("MEMBER_WRONG_PASSWORD");
    }

    @DisplayName("회원 정보를 삭제한다")
    @Test
    void delete() {
        Long memberId = createMember();
        memberService.deleteById(memberId);

        assertAll(
                () -> assertThat(tokenRepository.findByMemberId(memberId)).isEmpty(),
                () -> assertThatThrownBy(() -> memberService.findById(memberId))
                        .isInstanceOf(MemberException.class)
                        .hasMessage("MEMBER_DELETED")

        );
    }
    @DisplayName("존재하지 않는 회원 정보를 삭제한다")
    @Test
    void deleteNotExistMember() {
        assertThatThrownBy(() -> memberService.findById(1000L))
                .isInstanceOf(MemberException.class)
                .hasMessage("멤버가 존재하지 않습니다.");
    }

    @DisplayName("회원 정보 삭제 시 참여한 모임 중 진행중인 모임이 있을 경우 모임에 탈퇴시킨다")
    @Test
    void deleteAndLeave() {
        Article article = saveArticle();
        Long memberId = createMember();
        Member member = memberFindService.findMember(memberId);
        article.participate(member);

        memberService.deleteById(memberId);

        List<Article> articles = articleFindService.findParticipatedArticles(member);
        assertThat(articles).isEmpty();
    }

    @DisplayName("회원 정보 삭제 시 주최한 모임 중 진행중인 모임이 있을 경우 탈퇴할 수 없다")
    @Test
    void deleteExistInProgressArticle() {
        Article article = saveArticle();

        assertThatThrownBy(() -> memberService.deleteById(savedHost.getId()))
                .isInstanceOf(MemberException.class)
                .hasMessage("MEMBER_DELETED_EXIST_IN_PROGRESS_GROUP");
    }

    private Long createMember() {
        SignUpRequest request = new SignUpRequest("pongchul", "pongchul1!", "인철");
        return memberService.signUp(request);
    }

    private Article saveArticle() {
        Article article = TO_GANGNAM.builder()
                .capacity(4)
                .toArticle(savedHost);
        return articleRepository.save(article);

    }
}
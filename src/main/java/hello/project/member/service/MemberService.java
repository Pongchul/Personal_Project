package hello.project.member.service;

import hello.project.article.domain.Article;
import hello.project.article.domain.participant.ParticipantRepository;
import hello.project.article.service.ArticleFindService;
import hello.project.auth.domain.TokenRepository;
import hello.project.auth.support.PasswordEncoder;
import hello.project.member.domain.*;
import hello.project.member.service.dto.request.ChangeNameRequest;
import hello.project.member.service.dto.request.ChangePasswordRequest;
import hello.project.member.service.dto.request.SignUpRequest;
import hello.project.member.service.dto.response.MemberResponseAssembler;
import hello.project.member.service.dto.response.MyInfoResponse;
import hello.project.member.exception.MemberErrorCode;
import hello.project.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static hello.project.member.exception.MemberErrorCode.*;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final ArticleFindService articleFindService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberFindService memberFindService;
    private final ParticipantRepository participantRepository;
    private final TokenRepository tokenRepository;


    @Transactional
    public Long signUp(SignUpRequest request) {
        UserId userId = UserId.userId(request.getUserId());
        UserName userName = UserName.from(request.getName());
        Password password = Password.encrypt(request.getPassword(), passwordEncoder);

        validateUserIdIsNotDuplicated(userId);
        validateUserNameIsNotDuplicated(userName);

        Member member = new Member(userId, password, userName);
        Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }


    public MyInfoResponse findById(Long id) {
        Member member = memberFindService.findMember(id);

        return MemberResponseAssembler.myInfoResponse(member);
    }

    @Transactional
    public void deleteById(Long id) {
        Member member = memberFindService.findMember(id);
        leaveProgressingArticle(member);
        tokenRepository.deleteByMemberId(member.getId());

        member.delete();
    }

    private void leaveProgressingArticle(Member member) {
        List<Article> progressingArticles = articleFindService.findParticipatedArticles(member)
                .stream()
                .filter(article -> !article.isFinishedRecruitment())
                .collect(Collectors.toList());
        validateMemberIsNotHost(member, progressingArticles);

        participantRepository.deleteAllByMemberIdInArticles(member.getId(), progressingArticles);
    }

    private void validateMemberIsNotHost(Member member, List<Article> articles) {
        if (isHost(member, articles)) {
            throw new MemberException(MEMBER_DELETED_EXIST_IN_PROGRESS_GROUP);
        }
    }

    private boolean isHost(Member member, List<Article> articles) {
        return articles.stream()
                .anyMatch(article -> article.isHost(member));
    }

    @Transactional
    public void updatePassword(Long id, ChangePasswordRequest request) {
        Member member = memberFindService.findMember(id);
        confirmPassword(member, request.getExistingPassword());
        member.changePassword(request.getNewPassword(), passwordEncoder);
    }

    @Transactional
    public void updateName(Long id, ChangeNameRequest request) {
        UserName userName = UserName.from(request.getName());
        validateUserNameIsNotDuplicated(userName);

        Member member = memberFindService.findMember(id);
        member.changeUserName(request.getName());
    }

    private void confirmPassword(Member member, String password) {
        String encryptedPassword = passwordEncoder.encrypt(password);
        if (member.isNotSamePassword(encryptedPassword)) {
            throw new MemberException(MEMBER_WRONG_PASSWORD);
        }
    }

    private void validateUserIdIsNotDuplicated(UserId userId) {
        if (memberRepository.existsByUserId(userId)) {
            throw new MemberException(SIGNUP_USER_ID_DUPLICATED);
        }
    }

    private void validateUserNameIsNotDuplicated(UserName userName) {
        if (memberRepository.existsByUserName(userName)) {
            throw new MemberException(SIGNUP_USER_NAME_DUPLICATED);
        }
    }
}

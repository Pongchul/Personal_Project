package hello.project.member.service;

import hello.project.auth.support.PasswordEncoder;
import hello.project.member.domain.*;
import hello.project.member.dto.request.ChangeNameRequest;
import hello.project.member.dto.request.ChangePasswordRequest;
import hello.project.member.dto.request.SignUpRequest;
import hello.project.member.dto.response.MemberResponseAssembler;
import hello.project.member.dto.response.MyInfoResponse;
import hello.project.member.exception.MemberErrorCode;
import hello.project.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberFindService memberFindService;


    @Transactional
    public Long signUp(SignUpRequest request) {
        UserId userId = UserId.userId(request.getUserId());
        UserName userName = UserName.from(request.getPassword());
        Password password = Password.encrypt(request.getPassword(), passwordEncoder);

        validateUserIdIsNotDuplicated(userId);
        validateUserNameIsNotDuplicated(userName);

        Member member = new Member(userId, password);
        Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }


    private void validateUserIdIsNotDuplicated(UserId userId) {
        if (memberRepository.existsByUserId(userId)) {
            throw new MemberException(MemberErrorCode.SIGNUP_USER_ID_DUPLICATED);
        }
    }

    private void validateUserNameIsNotDuplicated(UserName userName) {
        if (memberRepository.existsByUserName(userName)) {
            throw new MemberException(MemberErrorCode.SIGNUP_USER_NAME_DUPLICATED);
        }
    }

    public MyInfoResponse findById(Long id) {
        Member member = memberFindService.findMember(id);

        return MemberResponseAssembler.myInfoResponse(member);
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
        Member member = memberFindService.findMember(id);

        member.changeUserName(request.getName());
    }

    private void confirmPassword(Member member, String password) {
        String encryptedPassword = passwordEncoder.encrypt(password);
        if (member.isNotSamePassword(encryptedPassword)) {
            throw new MemberException(MemberErrorCode.MEMBER_WRONG_PASSWORD);
        }
    }
}

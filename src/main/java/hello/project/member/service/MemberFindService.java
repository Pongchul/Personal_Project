package hello.project.member.service;

import hello.project.member.domain.Member;
import hello.project.member.domain.MemberRepository;
import hello.project.member.domain.Password;
import hello.project.member.domain.UserId;
import hello.project.member.exception.MemberErrorCode;
import hello.project.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hello.project.member.exception.MemberErrorCode.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberFindService {

    private final MemberRepository memberRepository;

    public Member findMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_EXIST));
        validateExistMember(member);
        return member;
    }

    public Member findByUserIdAndPassword(UserId userId, Password password) {
        Member member = memberRepository.findByUserIdAndPassword(userId, password)
                .orElseThrow(() -> new MemberException(MEMBER_INVALID_ID_AND_PASSWORD));
        validateExistMember(member);
        return member;
    }

    private void validateExistMember(Member member) {
        if (member.isDeleted()) {
            throw new MemberException(MEMBER_DELETED);
        }
    }
}

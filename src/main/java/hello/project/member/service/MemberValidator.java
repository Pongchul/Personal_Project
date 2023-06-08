package hello.project.member.service;

import hello.project.member.domain.Member;
import hello.project.member.domain.MemberRepository;
import hello.project.member.exception.MemberErrorCode;
import hello.project.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberValidator {

    private final MemberRepository memberRepository;

    public void validateExistMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_EXIST));

        if (member.isDeleted()) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_EXIST);
        }
    }
}

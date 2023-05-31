package hello.project.auth.service;

import hello.project.auth.domain.Token;
import hello.project.auth.domain.TokenRepository;
import hello.project.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TokenService {

    private final TokenRepository tokenRepository;

    @Transactional
    public void synchronizeRefreshToken(Member member, String refreshToken) {
        tokenRepository.findByMemberId(member.getId())
                .ifPresentOrElse(
                        token -> token.updateRefreshToken(refreshToken),
                        () -> tokenRepository.save(new Token(member, refreshToken))
                );

    }
    @Transactional
    public void deleteByMemberId(Long memberId) {
        tokenRepository.deleteByMemberId(memberId);
    }
}

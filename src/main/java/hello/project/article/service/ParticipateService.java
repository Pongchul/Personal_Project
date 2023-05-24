package hello.project.article.service;

import hello.project.article.domain.Article;
import hello.project.member.domain.Member;
import hello.project.member.service.MemberFindService;
import hello.project.member.service.dto.response.MemberResponse;
import hello.project.member.service.dto.response.MemberResponseAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ParticipateService {

    private final MemberFindService memberFindService;
    private final ArticleFindService articleFindService;

    @Transactional
    public void participate(Long articleId, Long memberId) {
        Article article = articleFindService.findArticle(articleId);
        Member member = memberFindService.findMember(memberId);

        article.participate(member);
    }

    public List<MemberResponse> findParticipants(Long articleId) {
        Article article = articleFindService.findByIdWithHost(articleId);
        List<Member> participants = article.getParticipants();

        return MemberResponseAssembler.memberResponses(participants);
    }

    @Transactional
    public void leave(Long articleId, Long memberId) {
        Article article = articleFindService.findArticle(articleId);
        Member participant = memberFindService.findMember(memberId);

        article.remove(participant);
    }
}

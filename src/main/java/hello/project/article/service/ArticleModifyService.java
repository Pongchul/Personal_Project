package hello.project.article.service;

import hello.project.article.domain.*;
import hello.project.article.domain.participant.Capacity;
import hello.project.article.domain.participant.ParticipantRepository;
import hello.project.article.exception.ArticleErrorCode;
import hello.project.article.exception.ArticleException;
import hello.project.article.service.dto.request.ArticleRequest;
import hello.project.article.service.dto.response.ArticleIdResponse;
import hello.project.article.service.dto.response.ArticleResponseAssembler;
import hello.project.member.domain.Member;
import hello.project.member.service.MemberFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.BiConsumer;

import static hello.project.article.exception.ArticleErrorCode.*;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ArticleModifyService {

    private final MemberFindService memberFindService;
    private final ArticleFindService articleFindService;
    private final ArticleRepository articleRepository;
    private final ParticipantRepository participantRepository;

    @Transactional
    public ArticleIdResponse create(Long memberId, ArticleRequest request) {
        Member host = memberFindService.findMember(memberId);

        Article article = new Article(host, request.getCurrentState(), request.getCapacity(), request.getTitle(),
                request.getLocation(), request.getDestination(), request.getContents());
        Article savedArticle = articleRepository.save(article);

        return ArticleResponseAssembler.articleIdResponse(savedArticle);
    }

    @Transactional
    public void update(Long hostId, Long articleId, ArticleRequest request) {
        ifMemberIsHost(hostId, articleId, (host, article) -> {
            updateArticle(article, request);

        });
    }
    public void updateArticle(Article article, ArticleRequest request) {
        Title title = request.getTitle();
        Capacity capacity = request.getCapacity();
        Location location = request.getLocation();
        Destination destination = request.getDestination();
        Contents contents = request.getContents();
        CurrentState currentState = request.getCurrentState();

        article.update(capacity, contents, location, title, destination, currentState);
    }

    @Transactional
    public void closeEarly(Long hostId, Long articleId) {
        ifMemberIsHost(hostId, articleId, (host, group) -> group.closeEarly());
    }

    @Transactional
    public void delete(Long hostId, Long articleId) {
        ifMemberIsHost(hostId, articleId, (host, article) -> {
            article.validateGroupIsProceeding();
            participantRepository.deleteAllByArticleId(articleId);
            articleRepository.deleteById(articleId);
        });
    }

    private void ifMemberIsHost(Long hostId, Long articleId, BiConsumer<Member, Article> consumer) {
        Member host = memberFindService.findMember(hostId);
        Article article = articleFindService.findArticle(articleId);

        validateMemberIsHost(article, host);

        consumer.accept(host, article);
    }

    private void validateMemberIsHost(Article article, Member member) {
        if (article.isNotHost(member)) {
            throw new ArticleException(MEMBER_IS_HOST);
        }
    }

}

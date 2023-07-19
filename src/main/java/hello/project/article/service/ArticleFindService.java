package hello.project.article.service;

import hello.project.article.domain.Article;
import hello.project.article.domain.search.ArticleSearchRepository;
import hello.project.article.exception.ArticleException;
import hello.project.member.domain.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static hello.project.article.exception.ArticleErrorCode.*;

@Service
@RequiredArgsConstructor
public class ArticleFindService {

    private final ArticleSearchRepository articleSearchRepository;

    public Article findArticle(Long id) {
        return articleSearchRepository.findById(id)
                .orElseThrow(() -> new ArticleException(NOT_EXIST));
    }

    public Article findByIdWithHost(Long id) {
        return articleSearchRepository.findByIdWithHost(id)
                .orElseThrow(() -> new ArticleException(NOT_EXIST));
    }

    public List<Article> findParticipatedArticles(Member member) {
        return articleSearchRepository.findParticipatedArticles(member);
    }
}

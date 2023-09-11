package hello.project.article.service;

import hello.project.article.domain.search.ArticleSearchRepository;
import hello.project.article.exception.ArticleErrorCode;
import hello.project.article.exception.ArticleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ArticleValidator {

    private final ArticleSearchRepository articleSearchRepository;

    public void validExistArticle(Long articleId) {
        boolean isExist = articleSearchRepository.existsById(articleId);
        if (!isExist) {
            throw new ArticleException(ArticleErrorCode.NOT_EXIST);
        }
    }
}

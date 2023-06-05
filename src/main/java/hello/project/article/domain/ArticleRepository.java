package hello.project.article.domain;

import hello.project.member.domain.Member;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends Repository<Article, Long> {

    Article save(Article article);

    void deleteById(Long id);
}

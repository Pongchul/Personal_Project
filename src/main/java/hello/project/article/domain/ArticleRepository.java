package hello.project.article.domain;

import hello.project.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findByHostAndId(Member host, Long Id);

    List<Article> findAllByHostAndCurrentStateNot(Member host, CurrentState currentState);

    List<Article> findAllByHostAndCurrentState(Member host, CurrentState currentState);

}

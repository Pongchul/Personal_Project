package hello.project.article.domain;

import hello.project.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findByAuthorAndId(Member author, Long Id);

    List<Article> findAllByAuthorAndCurrentStateNot(Member author, CurrentState currentState);

    List<Article> findAllByAuthorAndCurrentState(Member author, CurrentState currentState);

}

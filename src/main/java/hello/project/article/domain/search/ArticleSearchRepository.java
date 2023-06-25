package hello.project.article.domain.search;

import hello.project.article.domain.Article;
import hello.project.member.domain.Member;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface ArticleSearchRepository extends Repository<Article, Long>, ArticleSearchRepositoryCustom {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<Article> findById(Long id);

    @Query("select a from Article a "
            + "join fetch a.participants.host "
            + "where a.id = :id")
    Optional<Article> findByIdWithHost(@Param("id") Long id);

    @Query("select a from Article a "
            + "where a.participants.host = :member "
            + "OR ( :member IN (SELECT p.member.id FROM Participant p WHERE p.article = a) )")
    List<Article> findParticipatedArticles(@Param("member") Member member);

    boolean existsById(Long id);
}

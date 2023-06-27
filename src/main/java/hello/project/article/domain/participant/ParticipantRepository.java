package hello.project.article.domain.participant;

import hello.project.article.domain.Article;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParticipantRepository extends Repository<Participant, Long> {

    @Modifying
    @Query("delete from Participant p where p.article.id = :articleId")
    void deleteAllByArticleId(@Param("articleId") Long articleId);

    @Modifying
    @Query("delete from Participant p where p.member.id = :memberId and p.article in (:articles)")
    void deleteAllByMemberIdInArticles(@Param("memberId") Long memberId, @Param("articles") List<Article> articles);
}

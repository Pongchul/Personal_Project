package hello.project.favorite.domain;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends Repository<Favorite, Long> {

    Favorite save(Favorite favorite);

    boolean existsByArticleIdAndMemberId(Long articleId, Long memberId);

    Optional<Favorite> findByArticleIdAndMemberId(Long articleId, Long memberId);

    List<Favorite> findAllByMemberId(Long memberId);

    void delete(Favorite favorite);

    @Modifying
    @Query("delete from Favorite f where f.articleId = :articleId")
    void deleteAllByArticleId(@Param("articleId") Long articleId);

    @Modifying
    @Query("delete from Favorite f where f.memberId = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);
}

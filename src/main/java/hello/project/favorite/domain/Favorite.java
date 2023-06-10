package hello.project.favorite.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "article_id", nullable = false)
    private Long articleId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    public Favorite(Long articleId, Long memberId) {
        this.articleId = articleId;
        this.memberId = memberId;
    }

    public boolean isSameArticle(Long articleId) {
        return this.articleId.equals(articleId);
    }


}

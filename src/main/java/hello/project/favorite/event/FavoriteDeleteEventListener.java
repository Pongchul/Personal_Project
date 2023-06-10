package hello.project.favorite.event;

import hello.project.article.event.ArticleDeleteEvent;
import hello.project.favorite.domain.FavoriteRepository;
import hello.project.member.event.MemberDeleteEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FavoriteDeleteEventListener {

    private final FavoriteRepository favoriteRepository;

    @EventListener
    public void deleteArticle(ArticleDeleteEvent event) {
        favoriteRepository.deleteAllByArticleId(event.getId());
    }

    @EventListener
    public void deleteMember(MemberDeleteEvent event) {
        favoriteRepository.deleteAllByMemberId(event.getId());
    }
}

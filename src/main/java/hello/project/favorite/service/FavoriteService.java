package hello.project.favorite.service;

import hello.project.article.exception.ArticleErrorCode;
import hello.project.article.exception.ArticleException;
import hello.project.article.service.ArticleValidator;
import hello.project.favorite.domain.Favorite;
import hello.project.favorite.domain.FavoriteRepository;
import hello.project.member.service.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteService {

    private final ArticleValidator articleValidator;
    private final MemberValidator memberValidator;
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public void like(Long articleId, Long memberId) {
        articleValidator.validExistArticle(articleId);
        memberValidator.validateExistMember(memberId);
        validateMemberNotYetLike(articleId, memberId);

        Favorite favorite = new Favorite(articleId, memberId);
        favoriteRepository.save(favorite);
    }

    private void validateMemberNotYetLike(Long articleId, Long memberId) {
        boolean isExist = favoriteRepository.existsByArticleIdAndMemberId(articleId, memberId);
        if (isExist) {
            throw new ArticleException(ArticleErrorCode.MEMBER_NOT_YET_LIKE);
        }
    }

    @Transactional
    public void cancel(Long articleId, Long memberId) {
        Favorite favorite = favoriteRepository.findByArticleIdAndMemberId(articleId, memberId)
                .orElseThrow(() -> new ArticleException(ArticleErrorCode.MEMBER_NOT_YET_LIKE));

        favoriteRepository.delete(favorite);

    }
}

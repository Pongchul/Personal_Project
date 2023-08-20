package hello.project.article.infrastructure;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import hello.project.article.domain.search.ArticleSearchRepositoryCustom;
import hello.project.article.domain.search.SearchCondition;
import hello.project.article.domain.search.dto.ArticleIdRepositoryResponse;
import hello.project.article.domain.search.dto.ArticleSummaryRepositoryResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static hello.project.article.domain.QArticle.*;
import static hello.project.article.domain.participant.QParticipant.*;
import static hello.project.member.domain.QMember.member;
import static hello.project.favorite.domain.QFavorite.favorite;

@Repository
public class ArticleSearchRepositoryImpl implements ArticleSearchRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final StatusFilter statusFilter;

    public ArticleSearchRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.statusFilter = new StatusFilter();
    }


    @Override
    public Page<ArticleSummaryRepositoryResponse> findArticles(SearchCondition condition, Pageable pageable) {

        List<Long> articleIds = queryFactory
                .select(article.id)
                .from(article)
                .where(statusFilter.filterByCondition(condition))
                .orderBy(orderByCurrentStateAsc(condition.orderByCurrentState()).toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<ArticleSummaryRepositoryResponse> articles = queryFactory
                .select(makeProjections())
                .from(article)
                .innerJoin(article.participants.host, member)
                .leftJoin(article.participants.participants, participant)
                .where(article.id.in(articleIds))
                .groupBy(article.id)
                .orderBy(orderByCurrentStateAsc(condition.orderByCurrentState()).toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(article.countDistinct())
                .from(article)
                .leftJoin(article.participants.participants, participant)
                .where(statusFilter.filterByCondition(condition));


        return PageableExecutionUtils.getPage(articles, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<ArticleSummaryRepositoryResponse> findHostedArticles(SearchCondition condition, Long memberId, Pageable pageable) {
        return findArticles(condition, pageable, () -> isHost(memberId));
    }


    @Override
    public Page<ArticleSummaryRepositoryResponse> findParticipantsArticles(SearchCondition condition, Long memberId, Pageable pageable) {
        return findArticles(condition, pageable, () -> isParticipated(memberId));
    }

    @Override
    public Page<ArticleSummaryRepositoryResponse> findLikedArticles(SearchCondition condition, Long memberId, Pageable pageable) {

        List<Long> likedArticleIds = findLikedArticleIds(condition, memberId, pageable);

        List<ArticleSummaryRepositoryResponse> articles = queryFactory
                .select(makeProjections()).distinct()
                .from(article)
                .innerJoin(article.participants.host, member)
                .leftJoin(article.participants.participants, participant)
                .fetchJoin()
                .where(article.id.in(likedArticleIds))
                .groupBy(article.id)
                .orderBy(orderByCurrentStateAsc(condition.orderByCurrentState()).toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(article.count())
                .from(article)
                .leftJoin(article.participants.participants, participant)
                .innerJoin(favorite).on(article.id.eq(favorite.articleId))
                .where(
                        favorite.articleId.eq(memberId),
                        statusFilter.filterByCondition(condition)
                );

        return PageableExecutionUtils.getPage(articles, pageable, countQuery::fetchOne);

    }

    private List<Long> findLikedArticleIds(SearchCondition condition, Long memberId, Pageable pageable) {
        return queryFactory
                .select(article.id)
                .from(article)
                .innerJoin(favorite).on(article.id.eq(favorite.articleId))
                .where(
                        favorite.memberId.eq(memberId),
                        statusFilter.filterByCondition(condition)
                )
                .orderBy(orderByCurrentStateAsc(condition.orderByCurrentState()).toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageNumber())
                .fetch();
    }

    private Page<ArticleSummaryRepositoryResponse> findArticles(SearchCondition condition, Pageable pageable, Supplier<BooleanExpression> mainCondition) {
        List<Long> articleIds = queryFactory
                .select(makeArticleIdRepositoryResponse())
                .distinct()
                .from(article)
                .innerJoin(article.participants.host, member)
                .leftJoin(article.participants.participants, participant)
                .where(
                        mainCondition.get(),
                        statusFilter.filterByCondition(condition)
                )
                .groupBy(article.id)
                .orderBy(orderByCurrentStateAsc(condition.orderByCurrentState()).toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(ArticleIdRepositoryResponse::getArticleId)
                .collect(Collectors.toUnmodifiableList());

        List<ArticleSummaryRepositoryResponse> articles = queryFactory
                .select(makeProjections())
                .from(article)
                .innerJoin(article.participants.host, member)
                .leftJoin(article.participants.participants, participant)
                .where(article.id.in(articleIds))
                .groupBy(article.id)
                .orderBy(orderByCurrentStateAsc(condition.orderByCurrentState()).toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(article.countDistinct())
                .from(article)
                .leftJoin(article.participants.participants, participant)
                .where(
                        mainCondition.get(),
                        statusFilter.filterByCondition(condition)
                );

        return PageableExecutionUtils.getPage(articles, pageable, countQuery::fetchOne);
    }

    private List<OrderSpecifier<?>> orderByCurrentStateAsc(boolean orderByCurrentStatus) {
        List<OrderSpecifier<?>> orderBy = new LinkedList<>();

        if (orderByCurrentStatus) {
            orderBy.add(article.currentState.stringValue().asc());
        }

        orderBy.add(orderByIdDesc());

        return orderBy;
    }

    private static ConstructorExpression<ArticleSummaryRepositoryResponse> makeProjections() {
        int host = 1;
        return Projections.constructor(ArticleSummaryRepositoryResponse.class,
                article.id,
                article.title.value,
                article.participants.host.Id,
                member.userName.value,
                article.currentState,
                article.participants.capacity.value,
                participant.count().intValue().add(host),
                article.closedEarly
                );
    }

    private OrderSpecifier<Long> orderByIdDesc() {
        return article.id.desc();
    }

    private BooleanExpression isParticipated(Long memberId) {
        return isHost(memberId).or(participant.member.Id.eq(memberId));
    }

    private BooleanExpression isHost(Long memberId) {
        return article.participants.host.Id.eq(memberId);
    }

    private ConstructorExpression<ArticleIdRepositoryResponse> makeArticleIdRepositoryResponse() {
        return Projections.constructor(ArticleIdRepositoryResponse.class, article.id);
    }
}

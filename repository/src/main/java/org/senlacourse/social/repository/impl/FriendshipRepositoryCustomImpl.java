package org.senlacourse.social.repository.impl;

import org.senlacourse.social.domain.Friendship;
import org.senlacourse.social.domain.FriendshipId_;
import org.senlacourse.social.domain.Friendship_;
import org.senlacourse.social.domain.User_;
import org.senlacourse.social.repository.FriendshipRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FriendshipRepositoryCustomImpl implements FriendshipRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    private Long getQueryTotalRows(Root<?> root, Predicate restriction) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        countQuery.getRoots().add(root);
        countQuery.select(builder.count(root));
        if (restriction != null) {
            countQuery.where(restriction);
        }
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private Root<Friendship> getFindAllByUserIdRoot(CriteriaQuery<Friendship> query) {
        Root<Friendship> root = query.from(Friendship.class);
        root.join(Friendship_.id).join(FriendshipId_.user);
        root.join(Friendship_.id).join(FriendshipId_.friend);

        return root;
    }

    public Page<Friendship> findAllByUserIdCriteria(Long userId, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Friendship> query = builder.createQuery(Friendship.class);
        Root<Friendship> root = getFindAllByUserIdRoot(query);
        Predicate restriction = builder.or(
                builder.equal(root.get(Friendship_.id).get(FriendshipId_.user).get(User_.id), userId),
                builder.equal(root.get(Friendship_.id).get(FriendshipId_.friend).get(User_.id), userId));

        Long totalRows = getQueryTotalRows(root, restriction);
        query.select(root)
                .where(restriction)
                .orderBy(QueryUtils.toOrders(
                        pageable.getSort(),
                        root,
                        builder));
        TypedQuery<Friendship> typedQuery = entityManager.createQuery(query);
        int firstResult = pageable.getPageNumber() * pageable.getPageSize();
        return new PageImpl<>(
                typedQuery
                        .setFirstResult(firstResult)
                        .setMaxResults(pageable.getPageSize())
                        .getResultList(),
                pageable,
                totalRows);
    }
}

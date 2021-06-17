package org.senlacourse.social.repository.impl;

import lombok.extern.log4j.Log4j;
import org.senlacourse.social.domain.FriendshipRequest;
import org.senlacourse.social.domain.FriendshipRequest_;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.domain.User_;
import org.senlacourse.social.repository.FriendshipRequestRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Log4j
public class FriendshipRequestRepositoryCustomImpl implements FriendshipRequestRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    private Predicate getSenderAndRecipientPredicate(CriteriaBuilder builder,
                                                     Long[] userId,
                                                     Join<FriendshipRequest, User>[] join) {
        return builder.and(
                builder.equal(join[0].get(User_.id), userId[0]),
                builder.equal(join[1].get(User_.id), userId[1]));

    }

    private Predicate getFindOneByBothUserIdsPredicate(CriteriaBuilder builder,
                                                       Long senderId,
                                                       Long recipientId,
                                                       Join<FriendshipRequest, User>[] join) {
        return builder.or(
                getSenderAndRecipientPredicate(
                        builder,
                        new Long[]{senderId, recipientId},
                        join),
                getSenderAndRecipientPredicate(
                        builder,
                        new Long[]{recipientId, senderId},
                        join));
    }

    @SuppressWarnings("unchecked")
    private CriteriaQuery<FriendshipRequest> getFindOneByBothUserIdsQuery(Long senderId, Long recipientId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<FriendshipRequest> query = builder.createQuery(FriendshipRequest.class);
        Root<FriendshipRequest> root = query.from(FriendshipRequest.class);
        Join<FriendshipRequest, User> joinSender = root.join(FriendshipRequest_.sender);
        Join<FriendshipRequest, User> joinRecipient = root.join(FriendshipRequest_.recipient);

        return query
                .select(root)
                .where(
                        getFindOneByBothUserIdsPredicate(builder, senderId, recipientId,
                                new Join[]{joinSender, joinRecipient}));
    }

    @SuppressWarnings({"NullableProblems"})
    @Override
    public Optional<FriendshipRequest> findOneByBothUserIds(Long senderId, Long recipientId) {
        try {
            return Optional.of(
                    entityManager
                            .createQuery(
                                    getFindOneByBothUserIdsQuery(senderId, recipientId))
                            .getSingleResult());
        } catch (NoResultException e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }
}

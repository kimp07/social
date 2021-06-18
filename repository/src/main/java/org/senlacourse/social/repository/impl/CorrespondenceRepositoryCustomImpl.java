package org.senlacourse.social.repository.impl;

import org.senlacourse.social.domain.Correspondence;
import org.senlacourse.social.domain.CorrespondenceId_;
import org.senlacourse.social.domain.Correspondence_;
import org.senlacourse.social.domain.TalkMessage_;
import org.senlacourse.social.domain.Talk_;
import org.senlacourse.social.domain.User_;
import org.senlacourse.social.projection.UnreadTalkMessagesGroupByTalkIdDto;
import org.senlacourse.social.repository.CorrespondenceRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CorrespondenceRepositoryCustomImpl implements CorrespondenceRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    private Long getTotalRows(Root<?> root, Predicate predicate, Expression<?> groupBy, CriteriaBuilder builder) {
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        query.getRoots().add(root);
        query.select(builder.count(root)).where(predicate).groupBy(groupBy);

        return entityManager.createQuery(query).getSingleResult();
    }

    private Root<Correspondence> getCountUnreadMessagesByUserIdGroupByTalkIdRoot(CriteriaQuery<?> query,
                                                                                 CriteriaBuilder builder) {
        Root<Correspondence> root = query.from(Correspondence.class);
        root.join(Correspondence_.id).join(CorrespondenceId_.user);
        root.join(Correspondence_.id).join(CorrespondenceId_.talkMessage).join(TalkMessage_.talk);

        return root;
    }

    @Override
    public Page<UnreadTalkMessagesGroupByTalkIdDto> findCountUnreadMessagesByUserIdGroupByTalkIdCriteria(Long userId,
                                                                                                         Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UnreadTalkMessagesGroupByTalkIdDto> query
                = builder.createQuery(UnreadTalkMessagesGroupByTalkIdDto.class);
        Root<Correspondence> root = getCountUnreadMessagesByUserIdGroupByTalkIdRoot(query, builder);

        Predicate predicate = builder.and(
                builder.equal(root.get(Correspondence_.id).get(CorrespondenceId_.user).get(User_.id), userId),
                builder.equal(root.get(Correspondence_.unread), true)
        );

        Expression<?> groupBy = root.get(Correspondence_.id).get(CorrespondenceId_.talkMessage).get(TalkMessage_.talk).get(Talk_.id);

        long totalRecords = getTotalRows(root, predicate, groupBy, builder);

        query.select(
                builder.construct(
                        UnreadTalkMessagesGroupByTalkIdDto.class,
                        builder.count(groupBy),
                        groupBy))
                .groupBy(groupBy)
                .where(predicate)
                .multiselect(builder.count(groupBy), groupBy);
        return new PageImpl<>(entityManager.createQuery(query).getResultList(), pageable, totalRecords);
    }
}

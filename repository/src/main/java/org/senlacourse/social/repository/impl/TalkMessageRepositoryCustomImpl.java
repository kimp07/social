package org.senlacourse.social.repository.impl;

import org.senlacourse.social.domain.TalkMessage;
import org.senlacourse.social.repository.TalkMessageRepositoryCustom;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

public class TalkMessageRepositoryCustomImpl implements TalkMessageRepositoryCustom {

    public static final String UPDATE_SET_UNREAD_FALSE = "update talk_messages as tm set unread = 0";
    public static final String SELECT_ALL = "select * from talk_messages";
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<TalkMessage> findByTalkMessageId(Long id) {
        TypedQuery<TalkMessage> query
                = entityManager.createQuery("select tm from TalkMessage tm where tm.id = ?", TalkMessage.class);
        EntityGraph<TalkMessage> entityGraph = entityManager.createEntityGraph(TalkMessage.class);
        entityGraph.addAttributeNodes("answeredMessage", "talk", "sender");
        query.setParameter(0, id).setHint("javax.persistence.fetchgraph", entityGraph);
        return Optional.ofNullable(query.getSingleResult());
    }
}

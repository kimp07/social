package org.senlacourse.social.repository;

import org.senlacourse.social.domain.TalkMessagesCache;
import org.senlacourse.social.domain.projection.ITalkMessagesCacheTalksCountView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TalkMessagesCacheRepository extends JpaRepository<TalkMessagesCache, Long> {

    @Query(value = "select tm.talkMessage.talk as talk, count(tm.talkMessage.talk.id) as talkMessageCount"
            + " from TalkMessagesCache tm where tm.recipient.id = :recipientId group by tm.talkMessage.talk.id")
    Page<ITalkMessagesCacheTalksCountView> findAllByRecipientIdGroupByTalkId(Long recipientId, Pageable pageable);

    @Query(value = "select tm.talkMessage.talk as talk, count(tm.talkMessage.talk.id) as talkMessageCount"
            + " from TalkMessagesCache tm where tm.recipient.id = :recipientId and tm.talkMessage.talk.id = :talkId"
            + " group by tm.talkMessage.talk.id")
    ITalkMessagesCacheTalksCountView getCountByRecipientIdAndTalkId(Long recipientId,
                                                                              Long talkId);

    @Query(value = "delete from TalkMessagesCache tmc where tmc.recipient.id = :recipientId and"
            + " tmc.talkMessage.talk.id = :talkId")
    void deleteAllByRecipientIdAndTalkId(Long recipientId, Long talkId);

    @Query(value = "delete from TalkMessagesCache tmc where tmc.recipient.id = :recipientId")
    void deleteAllByRecipientId(Long recipientId);
}
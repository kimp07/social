package org.senlacourse.social.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import senlacourse.social.domain.TalkMessagesCache;

@Repository
public interface TalkMessagesCacheRepository extends JpaRepository<TalkMessagesCache, Long> {

    @Query(value = "select tm.talkMessage.talk.id as talkMessageId, count(tm.talkMessage.talk.id) as talkMessageCount from TalkMessagesCache tm where tm.recipient.id = :recipientId group by tm.talkMessage.talk.id")
    Page<ITalkMessageCacheCountByTalk> findAllByRecipientIdGroupByTalkId(Long recipientId, Long talkId, Pageable pageable);

    @Query(value = "delete from TalkMessagesCache tmc where tmc.recipient.id = :recipientId and tmc.talkMessage.talk.id = :talkId")
    void deleteAllByRecipientIdAndTalkId(Long recipientId, Long talkId);

    @Query(value = "delete from TalkMessagesCache tmc where tmc.recipient.id = :recipientId")
    void deleteAllByRecipientIdAndTalkId(Long recipientId);
}
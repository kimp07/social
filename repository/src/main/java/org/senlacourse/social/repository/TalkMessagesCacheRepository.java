package org.senlacourse.social.repository;

import org.senlacourse.social.domain.projection.IUnreadTalkMessagesGroupByTalkIdCountView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TalkMessagesCacheRepository extends JpaRepository<TalkMessagesCache, Long> {

    @Query("select t.id as talkId, count(t) as talkMessagesCount from TalkMessagesCache tmc "
            + "left join TalkMessage tm on tmc.talkMessage.id = tm.id left join Talk t on t.id=tm.talk.id "
            + "where tmc.recipient.id = :recipientId group by t.id")
    Page<IUnreadTalkMessagesGroupByTalkIdCountView> findAllByRecipientIdGroupByTalkId(Long recipientId, Pageable pageable);

    @Query("select t.id as talkId, count(t) as talkMessagesCount from TalkMessagesCache tmc "
            + "left join TalkMessage tm on tmc.talkMessage.id = tm.id left join Talk t on t.id=tm.talk.id "
            + "where tmc.recipient.id = :recipientId and t.id = :talkId group by t.id")
    IUnreadTalkMessagesGroupByTalkIdCountView getCountByRecipientIdAndTalkId(Long recipientId,
                                                                             Long talkId);

    @Query(value = "delete from TalkMessagesCache tmc where tmc.recipient.id = :recipientId and"
            + " tmc.talkMessage.talk.id = :talkId")
    void deleteAllByRecipientIdAndTalkId(Long recipientId, Long talkId);

    @Query(value = "delete from TalkMessagesCache tmc where tmc.recipient.id = :recipientId")
    void deleteAllByRecipientId(Long recipientId);
}
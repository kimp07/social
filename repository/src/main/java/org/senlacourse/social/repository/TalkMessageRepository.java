package org.senlacourse.social.repository;

import org.senlacourse.social.domain.TalkMessage;
import org.senlacourse.social.domain.projection.IUnreadTalkMessagesGroupByTalkIdCountView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TalkMessageRepository extends JpaRepository<TalkMessage, Long> {

    @EntityGraph(attributePaths = {"user", "answeredMessage"})
    Page<TalkMessage> findAllByTalkId(Long talkId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "answeredMessage"})
    Page<TalkMessage> findAllBySenderIdAndTalkIdAndUnreadIsTrue(Long senderId, Long talkId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "answeredMessage", "talk", "sender"})
    @Override
    Optional<TalkMessage> findById(Long id);

    @Query("select t.id as talkId, count(t.id) as talkMessagesCount from TalkMessage tm "
            + "join User u on tm.user.id = u.id join Talk t on tm.talk.id = t.id where u.id = :userId and tm.unread "
            + "group by t.id")
    Optional<IUnreadTalkMessagesGroupByTalkIdCountView> findCountByUserIdAndUnreadIsTrueGroupByTalkId(Long userId);

    @Query("select count(u.id) as talkMessagesCount from TalkMessage tm "
            + "join User u on tm.user.id = u.id where u.id = :userId and tm.unread group by t.id")
    Optional<Long> findCountByUserIdAndUnreadIsTrue(Long userId);

    @Query("update TalkMessage tm set tm.unread = FALSE where tm.unread = TRUE and tm.user.id = :userId")
    void updateAllSetUnreadFalseByUserId(Long userId);

    @Query("update TalkMessage tm set tm.unread = FALSE where tm.unread = TRUE and tm.user.id = :userId and tm.talk.id = :talkId")
    void updateAllSetUnreadFalseByUserAndTalkId(Long userId, Long talkId);

    @Query("update TalkMessage tm set tm.unread = FALSE where tm.id in :id")
    void updateSetUnreadFalseById(Long[] id);
}
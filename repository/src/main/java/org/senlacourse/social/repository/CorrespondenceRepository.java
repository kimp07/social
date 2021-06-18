package org.senlacourse.social.repository;

import org.senlacourse.social.domain.Correspondence;
import org.senlacourse.social.domain.CorrespondenceId;
import org.senlacourse.social.projection.IUnreadTalkMessagesView;
import org.senlacourse.social.projection.UnreadTalkMessagesGroupByTalkIdCountView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CorrespondenceRepository extends JpaRepository<Correspondence, CorrespondenceId> {

    Page<Correspondence> findAllByIdUserIdAndIdTalkMessageTalkIdOrderByIdTalkMessageId(Long userId,
                                                                                       Long talkMessageId,
                                                                                       Pageable pageable);

    @Query("select count(c.unread) as unreadMessagesCount from Correspondence c where c.id.user.id = :userId and c.unread = true")
    IUnreadTalkMessagesView findCountUnreadMessagesByUserId(Long userId);

    @Query("update Correspondence c set c.unread = false where c.id.user.id = :userId and c.id.talkMessage.talk.id = :talkId and c.unread = true")
    void updateAllSetUnreadFalseByUserIdAndTalkId(@Param("userId") Long userId, @Param("talkId") Long talkId);

    @Query("update Correspondence c set c.unread = false where c.id.user.id = :userId")
    void updateAllSetUnreadFalseByUserId(Long userId);

    void deleteByIdUserIdAndIdTalkMessageId(Long userId, Long talkMessageId);

    @Query("select c.id.talkMessage.talk.id as talkId, count(c.id.talkMessage.talk.id) as talkMessagesCount from Correspondence c "
    + " where c.id.user.id = :userId and c.unread = true group by c.id.talkMessage.talk.id")
    Page<UnreadTalkMessagesGroupByTalkIdCountView> findCountUnreadMessagesByUserIdGroupByTalkId(Long userId,
                                                                                                Pageable pageable);
}

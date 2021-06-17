package org.senlacourse.social.service;

import org.senlacourse.social.projection.IUnreadTalkMessagesView;
import org.senlacourse.social.projection.UnreadTalkMessagesGroupByTalkIdCountView;
import org.senlacourse.social.dto.CorrespondenceDto;
import org.senlacourse.social.dto.UserIdDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICorrespondenceService {

    Page<CorrespondenceDto> findAllByUserIdAndTalkId(UserIdDto dto, Long talkId, Pageable pageable);

    Page<UnreadTalkMessagesGroupByTalkIdCountView> getCountUnreadMessagesByUserIdGroupByTalkId(UserIdDto dto,
                                                                                               Long talkId,
                                                                                               Pageable pageable);

    IUnreadTalkMessagesView getCountUnreadMessagesByUserId(UserIdDto dto);

    void updateMessagesSetUnreadFalseByRecipientId(UserIdDto dto);

    void updateMessagesSetUnreadFalseByRecipientIdAndTalkId(UserIdDto dto, Long talkId);
}

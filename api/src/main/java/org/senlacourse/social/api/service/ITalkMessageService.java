package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.domain.TalkMessage;
import org.senlacourse.social.domain.projection.IUnreadTalkMessagesGroupByTalkIdCountView;
import org.senlacourse.social.dto.NewTalkMessageDto;
import org.senlacourse.social.dto.TalkMessageDto;
import org.senlacourse.social.dto.UserIdDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITalkMessageService extends IService<TalkMessage> {

    Page<TalkMessageDto> findAllByTalkId(Long talkId, Pageable pageable) throws ObjectNotFoundException;

    void addNewMessage(NewTalkMessageDto dto)
            throws ObjectNotFoundException, ServiceException;

    Page<IUnreadTalkMessagesGroupByTalkIdCountView> findCacheMessagesByRecipientIdAndTalkId(UserIdDto dto, Pageable pageable)
            throws ObjectNotFoundException;

    IUnreadTalkMessagesGroupByTalkIdCountView findCacheMessagesCountByRecipientIdAndTalkId(UserIdDto dto, Long talkId)
            throws ObjectNotFoundException;

    void deleteCacheMessagesByRecipientId(UserIdDto dto);

    void deleteCacheMessagesByRecipientIdAndTalkId(UserIdDto dto, Long talkId);
}

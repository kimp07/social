package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.domain.projection.ITalkMessagesCacheTalksCountView;
import org.senlacourse.social.dto.NewTalkMessageDto;
import org.senlacourse.social.dto.TalkMessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITalkMessageService {

    Page<TalkMessageDto> findAllByTalkId(Long talkId, Pageable pageable) throws ObjectNotFoundException;

    TalkMessageDto addNewMessage(NewTalkMessageDto dto)
            throws ObjectNotFoundException, ServiceException;

    Page<ITalkMessagesCacheTalksCountView> findCacheMessagesByRecipientIdGroupByTalkId(Long recipientId,
                                                                                       Pageable pageable)
            throws ObjectNotFoundException;

    ITalkMessagesCacheTalksCountView findCacheMessagesCountByRecipientIdAndTalkId(Long recipientId,Long talkId)
            throws ObjectNotFoundException;

    void deleteCacheMessagesByRecipientId(Long recipientId);

    void deleteCacheMessagesByRecipientIdAndTalkId(Long recipientId, Long talkId);
}

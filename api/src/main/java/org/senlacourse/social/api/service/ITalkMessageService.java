package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.domain.TalkMessage;
import org.senlacourse.social.domain.projection.ITalkMessagesCacheTalksCountView;
import org.senlacourse.social.dto.NewTalkMessageDto;
import org.senlacourse.social.dto.TalkMessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITalkMessageService extends IService<TalkMessage> {

    Page<TalkMessageDto> findAllByTalkId(Long talkId, Pageable pageable) throws ObjectNotFoundException;

    TalkMessageDto addNewMessage(NewTalkMessageDto dto)
            throws ObjectNotFoundException, ServiceException;

    Page<ITalkMessagesCacheTalksCountView> findCacheMessagesByRecipientIdGroupByTalkId(Long userId,
                                                                                       Pageable pageable)
            throws ObjectNotFoundException;

    ITalkMessagesCacheTalksCountView findCacheMessagesCountByRecipientIdAndTalkId(Long userId,Long talkId)
            throws ObjectNotFoundException;

    void deleteCacheMessagesByRecipientId(Long userId);

    void deleteCacheMessagesByRecipientIdAndTalkId(Long userId, Long talkId);
}

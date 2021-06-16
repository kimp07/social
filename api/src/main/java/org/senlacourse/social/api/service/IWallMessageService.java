package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.domain.WallMessage;
import org.senlacourse.social.dto.EditMessageDto;
import org.senlacourse.social.dto.NewWallMessageDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.dto.WallMessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IWallMessageService extends IService<WallMessage> {

    Page<WallMessageDto> findAllBySocietyId(Long wallId, Pageable pageable);

    void deleteAllMessagesBySocietyIdAndUserId(UserIdDto dto, Long wallId) throws ObjectNotFoundException, ServiceException;

    void deleteByMessageIdAndUserId(UserIdDto dto, Long wallMessageId) throws ObjectNotFoundException, ServiceException;

    WallMessageDto addNewMessage(NewWallMessageDto dto) throws ObjectNotFoundException, ServiceException;

    WallMessageDto editWallMessage(EditMessageDto dto) throws ObjectNotFoundException, ServiceException;

    void addLikeToMessage(Long messageId) throws ObjectNotFoundException, ServiceException;

    void addDislikeToMessage(Long messageId) throws ObjectNotFoundException, ServiceException;
}

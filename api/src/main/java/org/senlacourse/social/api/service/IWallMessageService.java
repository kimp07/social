package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.dto.EditMessageDto;
import org.senlacourse.social.dto.NewWallMessageDto;
import org.senlacourse.social.dto.WallMessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IWallMessageService {

    Page<WallMessageDto> findAllByWallId(Long wallId, Pageable pageable);

    void deleteAllMessagesByWallIdAndUserId(Long wallId, Long userId) throws ObjectNotFoundException, ServiceException;

    void deleteWallMessageByIdAndUserId(Long wallMessageId, Long userId) throws ObjectNotFoundException, ServiceException;

    WallMessageDto addNewMessge(NewWallMessageDto dto) throws ObjectNotFoundException, ServiceException;

    WallMessageDto editWallMessage(EditMessageDto dto) throws ObjectNotFoundException, ServiceException;

    void addLikeToMessage(Long userId, Long messageId) throws ObjectNotFoundException, ServiceException;

    void addDislikeToMessage(Long userId, Long messageId) throws ObjectNotFoundException, ServiceException;
}

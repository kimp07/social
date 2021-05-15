package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.dto.EditMessageDto;
import org.senlacourse.social.dto.NewWallMessageDto;
import org.senlacourse.social.dto.WallMessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IWallMessageService {

    Page<WallMessageDto> findAllByWallId(Long wallId, Pageable pageable);

    void deleteAllMessagesByWallIdAndUserId(Long wallId, Long userId) throws ObjectNotFoundException;

    void deleteWallMessageByIdAndUserId(Long wallMessageId, Long userId) throws ObjectNotFoundException, ServiceException;

    Optional<WallMessageDto> addNewMessge(NewWallMessageDto dto) throws ObjectNotFoundException, ServiceException;

    Optional<WallMessageDto> editWallMessage(EditMessageDto dto) throws ObjectNotFoundException, ServiceException;

    void addLikeToMessage(Long userId, Long messageId) throws ObjectNotFoundException;

    void addDislikeToMessage(Long userId, Long messageId) throws ObjectNotFoundException;
}

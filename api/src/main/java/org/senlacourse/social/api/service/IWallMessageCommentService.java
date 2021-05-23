package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.dto.EditMessageDto;
import org.senlacourse.social.dto.NewWallMessageCommentDto;
import org.senlacourse.social.dto.WallMessageCommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IWallMessageCommentService {

    Page<WallMessageCommentDto> findAllByWallMessageId(Long wallMessageId, Pageable pageable);

    void deleteAllByMessageId(Long messageId, Long userId) throws ObjectNotFoundException, ServiceException;

    void deleteByCommentIdAndUserId(Long wallMessageCommentId, Long userId) throws ObjectNotFoundException;

    WallMessageCommentDto editWallMessageComment(EditMessageDto dto)
            throws ObjectNotFoundException, ServiceException;

    WallMessageCommentDto addNewWallMessageComment(NewWallMessageCommentDto dto)
            throws ObjectNotFoundException, ServiceException;

    void addLikeToMessage(Long userId, Long messageId) throws ObjectNotFoundException, ServiceException;

    void addDislikeToMessage(Long userId, Long messageId) throws ObjectNotFoundException, ServiceException;
}

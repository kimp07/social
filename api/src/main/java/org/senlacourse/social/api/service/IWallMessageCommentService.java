package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.domain.WallMessageComment;
import org.senlacourse.social.dto.EditMessageDto;
import org.senlacourse.social.dto.NewWallMessageCommentDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.dto.WallMessageCommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IWallMessageCommentService extends IService<WallMessageComment> {

    Page<WallMessageCommentDto> findAllByWallMessageId(Long wallMessageId, Pageable pageable);

    void deleteAllByMessageId(UserIdDto dto, Long messageId) throws ObjectNotFoundException, ServiceException;

    void deleteByCommentIdAndUserId(UserIdDto dto, Long wallMessageCommentId) throws ObjectNotFoundException;

    WallMessageCommentDto editWallMessageComment(EditMessageDto dto)
            throws ObjectNotFoundException, ServiceException;

    WallMessageCommentDto addNewWallMessageComment(NewWallMessageCommentDto dto)
            throws ObjectNotFoundException, ServiceException;

    void addLikeToMessage(Long messageId) throws ObjectNotFoundException, ServiceException;

    void addDislikeToMessage(Long messageId) throws ObjectNotFoundException, ServiceException;
}

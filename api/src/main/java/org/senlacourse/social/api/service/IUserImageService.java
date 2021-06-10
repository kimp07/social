package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.dto.NewUserImageDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.dto.UserImageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserImageService {
    UserImageDto findByUserIdAndImageId(Long userId, Long imageId) throws ObjectNotFoundException;

    @AuthorizedUser
    Page<UserImageDto> findAllImagesByUserId(UserIdDto dto, Pageable pageable) throws ObjectNotFoundException;

    @AuthorizedUser
    void deleteByUserImageIdAndUserId(UserIdDto dto, Long userImageId)
            throws ObjectNotFoundException, ServiceException;

    @AuthorizedUser
    void deleteAllByUserId(UserIdDto dto) throws ObjectNotFoundException;

    @AuthorizedUser
    UserImageDto save(NewUserImageDto dto) throws ObjectNotFoundException;

    @AuthorizedUser
    void setImageToUserAvatar(UserIdDto dto, Long imageId) throws ObjectNotFoundException;
}

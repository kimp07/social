package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.domain.UserImage;
import org.senlacourse.social.dto.NewUserImageDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.dto.UserImageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserImageService extends IService<UserImage> {

    UserImageDto findById(Long id) throws ObjectNotFoundException;

    Page<UserImageDto> findAllImagesByUserId(UserIdDto dto, Pageable pageable) throws ObjectNotFoundException;

    void deleteByUserImageIdAndUserId(UserIdDto dto, Long userImageId)
            throws ObjectNotFoundException, ServiceException;

    void deleteAllByUserId(UserIdDto dto) throws ObjectNotFoundException;

    UserImageDto save(NewUserImageDto dto) throws ObjectNotFoundException;

    void setImageToUserAvatar(UserIdDto dto, Long imageId) throws ObjectNotFoundException;
}

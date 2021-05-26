package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.service.IUserImageService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.domain.UserImage;
import org.senlacourse.social.dto.NewUserImageDto;
import org.senlacourse.social.dto.UserImageDto;
import org.senlacourse.social.mapstruct.UserDtoMapper;
import org.senlacourse.social.mapstruct.UserImageDtoMapper;
import org.senlacourse.social.repository.UserImageRepository;
import org.senlacourse.social.security.service.AuthorizedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
@Log4j
public class UserImageService extends AbstractService<UserImage> implements IUserImageService {

    private final UserImageRepository userImageRepository;
    private final UserDtoMapper userDtoMapper;
    private final IUserService userService;
    private final UserImageDtoMapper userImageDtoMapper;

    @Override
    public UserImage findEntityById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(
                userImageRepository
                        .findById(id)
                        .orElse(null));
    }

    @Override
    public UserImageDto findById(Long id) throws ObjectNotFoundException {
        return userImageDtoMapper.fromEntity(
                findEntityById(id));
    }

    @AuthorizedUser
    @Override
    public Page<UserImageDto> findAllImagesByUserId(Long userId, Pageable pageable) throws ObjectNotFoundException {
        return userImageDtoMapper.map(
                userImageRepository.findAllByUserId(userId, pageable));
    }

    @AuthorizedUser
    @Override
    public void deleteByUserImageIdAndUserId(Long userId, Long userImageId)
            throws ObjectNotFoundException, ServiceException {
        UserImage userImage = findEntityById(userImageId);
        if (userImage.getUser().getId().equals(userId)) {
            userImageRepository.deleteById(userImageId);
        } else {
            ServiceException e = new ServiceException("User id=" + userId + " can`t delete image id=" + userImageId);
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @AuthorizedUser
    @Override
    public void deleteAllByUserId(Long userId) throws ObjectNotFoundException {
        User user = userService.findEntityById(userId);
        user.setUserImageFileName("");
        userService.updateUser(userDtoMapper.fromEntity(user));
        userImageRepository.deleteAllByUserId(userId);
    }

    @AuthorizedUser
    @Override
    public UserImageDto save(NewUserImageDto dto) throws ObjectNotFoundException {
        UserImage userImage = new UserImage()
                .setUser(
                        userService.findEntityById(dto.getUserId()))
                .setImgFileName(dto.getImgFileName());
        return userImageDtoMapper.fromEntity(
                userImageRepository.save(userImage));
    }

    @AuthorizedUser
    @Override
    public void setImageToUserAvatar(Long userId, Long imageId) throws ObjectNotFoundException {
        User user = userService.findEntityById(userId);
        UserImage image = findEntityById(imageId);
        user.setUserImageFileName(image.getImgFileName());
        userService.updateUser(userDtoMapper.fromEntity(user));
    }
}

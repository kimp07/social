package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.service.IImageService;
import org.senlacourse.social.api.service.IUserImageService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.domain.Image;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.domain.UserImage;
import org.senlacourse.social.domain.UserImagePk;
import org.senlacourse.social.dto.NewUserImageDto;
import org.senlacourse.social.dto.UserIdDto;
import org.senlacourse.social.dto.UserImageDto;
import org.senlacourse.social.mapstruct.UserDtoMapper;
import org.senlacourse.social.mapstruct.UserImageDtoMapper;
import org.senlacourse.social.repository.UserImageRepository;
import org.senlacourse.social.security.service.AuthorizedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
@Log4j
public class UserImageService implements IUserImageService {

    private final UserImageRepository userImageRepository;
    private final UserDtoMapper userDtoMapper;
    private final IUserService userService;
    private final IImageService imageService;
    private final UserImageDtoMapper userImageDtoMapper;

    @Override
    public UserImageDto findByUserIdAndImageId(Long userId, Long imageId) throws ObjectNotFoundException {
        return userImageDtoMapper.fromEntity(
                userImageRepository
                        .findByIdUserIdAndIdImageId(userId, imageId)
                        .orElseThrow(ObjectNotFoundException::new));
    }

    @AuthorizedUser
    @Override
    public Page<UserImageDto> findAllImagesByUserId(UserIdDto dto, Pageable pageable) throws ObjectNotFoundException {
        return userImageDtoMapper.map(
                userImageRepository.findAllByIdUserId(dto.getAuthorizedUserId(), pageable));
    }

    @AuthorizedUser
    @Override
    public void deleteByUserImageIdAndUserId(UserIdDto dto, Long userImageId)
            throws ObjectNotFoundException, ServiceException {
        UserImage image = userImageRepository
                .findByIdUserIdAndIdImageId(dto.getAuthorizedUserId(), userImageId)
                .orElseThrow(() -> {
                    throw new ObjectNotFoundException("Object not found");
                });
        if (image.getId().getUser().getId().equals(dto.getAuthorizedUserId())) {
            userImageRepository.deleteById(image.getId());
        } else {
            ServiceException e
                    = new ServiceException("User id=" + dto.getAuthorizedUserId() + " can`t delete image id=" + userImageId);
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @AuthorizedUser
    @Override
    public void deleteAllByUserId(UserIdDto dto) throws ObjectNotFoundException {
        User user = userService.findEntityById(dto.getAuthorizedUserId());
        user.setAvatar(null);
        userService.updateUser(userDtoMapper.fromEntity(user));
        userImageRepository.deleteAllByIdUserId(dto.getAuthorizedUserId());
    }

    @AuthorizedUser
    @Override
    public UserImageDto save(NewUserImageDto dto) throws ObjectNotFoundException {
        Image image = new Image()
                .setImgFileName(dto.getImgFileName());
        User user = userService.findEntityById(dto.getUserId());
        return userImageDtoMapper.fromEntity(
                userImageRepository
                        .save(new UserImage()
                                .setId(new UserImagePk()
                                        .setUser(user)
                                        .setImage(image))));
    }

    @AuthorizedUser
    @Override
    public void setImageToUserAvatar(UserIdDto dto, Long imageId) throws ObjectNotFoundException {
        User user = userService.findEntityById(dto.getAuthorizedUserId());
        Image image = imageService.findEntityById(imageId);
        user.setAvatar(image);
        userService.updateUser(userDtoMapper.fromEntity(user));
    }
}

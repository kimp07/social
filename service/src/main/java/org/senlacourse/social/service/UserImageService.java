package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.security.IAuthorizedUserService;
import org.senlacourse.social.api.service.IUserImageService;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.domain.UserImage;
import org.senlacourse.social.dto.NewUserImageDto;
import org.senlacourse.social.dto.UserImageDto;
import org.senlacourse.social.mapstruct.UserImageDtoMapper;
import org.senlacourse.social.repository.UserImageRepository;
import org.senlacourse.social.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j
@Transactional
public class UserImageService extends AbstractService<UserImage> implements IUserImageService {

    private final UserImageRepository userImageRepository;
    private final UserRepository userRepository;
    private final UserImageDtoMapper userImageDtoMapper;
    private final IAuthorizedUserService authorizedUserService;

    @Override
    UserImage findEntityById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(
                userImageRepository.findById(id).orElse(null),
                "User image not defined for id=" + id);
    }

    private User findUserById(Long userId) throws ObjectNotFoundException {
        return validateEntityNotNull(
                userRepository.findById(userId).orElse(null),
                "User not defined for id=" + userId);
    }

    @Override
    public UserImageDto findById(Long id) throws ObjectNotFoundException {
        return userImageDtoMapper.fromEntity(
                findEntityById(id));
    }

    @Override
    public Page<UserImageDto> findAllImagesByUserId(Long userId, Pageable pageable) throws ObjectNotFoundException {
        authorizedUserService.injectAuthorizedUserId(userId);
        return userImageDtoMapper.map(
                userImageRepository.findAllByUserId(userId, pageable));
    }

    @Override
    public void deleteByUserImageIdAndUserId(Long userImageId, Long userId)
            throws ObjectNotFoundException, ServiceException {
        authorizedUserService.injectAuthorizedUserId(userId);
        UserImage userImage = findEntityById(userImageId);
        if (userImage.getUser().getId().equals(userId)) {
            userImageRepository.deleteById(userImageId);
        } else {
            ServiceException e = new ServiceException("User id=" + userId + " can`t delete image id=" + userImageId);
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deleteAllByUserId(Long userId) throws ObjectNotFoundException {
        authorizedUserService.injectAuthorizedUserId(userId);
        User user = findUserById(userId);
        user.setUserImageFileName("");
        userRepository.save(user);
        userImageRepository.deleteAllByUserId(userId);
    }

    @Override
    public UserImageDto save(NewUserImageDto dto) throws ObjectNotFoundException {
        authorizedUserService.injectAuthorizedUserId(dto);
        UserImage userImage = new UserImage()
                .setUser(
                        findUserById(dto.getUserId()))
                .setImgFileName(dto.getImgFileName());
        return userImageDtoMapper.fromEntity(
                userImageRepository.save(userImage));
    }
}

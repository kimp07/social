package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.security.IAuthorizedUserService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.api.util.SqlUtil;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.dto.NewUserDto;
import org.senlacourse.social.dto.UpdateUserDto;
import org.senlacourse.social.dto.UserDto;
import org.senlacourse.social.dto.UserPasswordDto;
import org.senlacourse.social.dto.UserSimpleDto;
import org.senlacourse.social.mapstruct.NewUserDtoMapper;
import org.senlacourse.social.mapstruct.UserDtoMapper;
import org.senlacourse.social.repository.RoleRepository;
import org.senlacourse.social.repository.UserRepository;
import org.senlacourse.social.security.service.AuthorizedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Log4j
public class UserService extends AbstractService<User> implements IUserService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserDtoMapper userDtoMapper;
    private final NewUserDtoMapper newUserDtoMapper;
    private final IAuthorizedUserService authorizedUserService;

    @Override
    public User findEntityById(Long id) throws ObjectNotFoundException {
        return validateEntityNotNull(
                userRepository
                        .findById(id)
                        .orElse(null));
    }

    private User findEntityByLogin(String login) throws ObjectNotFoundException {
        return validateEntityNotNull(
                userRepository
                        .findOneByUserLogin(login)
                        .orElse(null));
    }

    @Override
    public UserDto findById(Long id) throws ObjectNotFoundException {
        return userDtoMapper
                .fromEntity(
                        findEntityById(id));
    }

    @Override
    public UserDto findByUserLogin(String userLogin) throws ObjectNotFoundException {
        return userDtoMapper
                .fromEntity(
                        findEntityByLogin(userLogin));
    }

    @Override
    public UserDto findByUserLoginAndPassword(String userLogin, String password)
            throws ObjectNotFoundException, ServiceException {
        User user = findEntityByLogin(userLogin);
        if (user.getPassword().equals(password)) {
            return userDtoMapper.fromEntity(user);
        } else {
            ServiceException e = new ServiceException("Incorrect password");
            log.warn(e);
            throw e;
        }
    }

    @Override
    public UserDto findByEmail(String email) throws ObjectNotFoundException {
        return userDtoMapper.fromEntity(
                validateEntityNotNull(
                        userRepository
                                .findOneByEmail(email)
                                .orElse(null)));
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return userDtoMapper.map(userRepository.findAll(pageable));
    }

    @Override
    public Page<UserDto> findAllByFirstNameAndSurname(String firstName, String surname, Pageable pageable) {
        firstName = SqlUtil.normalizeLikeFilter(firstName);
        surname = SqlUtil.normalizeLikeFilter(surname);
        return userDtoMapper.map(userRepository.findAllByFirstNameAndSurname(firstName, surname, pageable));
    }

    private User handleNewUserDto(NewUserDto dto) throws ServiceException {
        User user = newUserDtoMapper.toEntity(dto)
                .setNonExpired(true)
                .setCredentialsNonExpired(true)
                .setNonLocked(true)
                .setEnabled(true);
        if (dto.getRoleId() == null) {
            ServiceException e = new ServiceException("Role if can`t be empty");
            log.error(e.getMessage(), e);
            throw e;
        }
        roleRepository.findById(dto.getRoleId()).ifPresent(user::setRole);
        return user;
    }

    @Transactional(rollbackFor = {Throwable.class})
    @Override
    public UserDto saveUser(NewUserDto dto) throws ObjectNotFoundException, ServiceException {
        User user = handleNewUserDto(dto);
        if (user.getRole() == null) {
            ObjectNotFoundException e = new ObjectNotFoundException("User role not defined");
            log.error(e.getMessage(), e);
            throw e;
        }
        return userDtoMapper.fromEntity(userRepository.save(user));
    }

    private User updateUser(User user) {
        return userRepository.save(user);
    }

    @Transactional(rollbackFor = {Throwable.class})
    @Override
    public UserDto updateUser(UserDto dto) throws ObjectNotFoundException {
        User userFromBase = findEntityById(dto.getId());
        User user = userDtoMapper.toEntity(dto);
        user.setPassword(userFromBase.getPassword());
        return userDtoMapper.fromEntity(updateUser(user));
    }

    @Transactional(rollbackFor = {Throwable.class})
    @Override
    public UserDto updateUser(UpdateUserDto dto) throws ObjectNotFoundException, ServiceException {
        authorizedUserService.injectAuthorizedUserId(dto);
        User userFromBase = findEntityById(dto.getId());
        userFromBase
                .setFirstName(dto.getFirstName())
                .setSurname(dto.getSurname())
                .setBirthDate(LocalDate.parse(dto.getBirthDate(), DATE_FORMATTER))
                .setAboutMe(dto.getAboutMe());
        return userDtoMapper.fromEntity(updateUser(userFromBase));
    }

    @Transactional(rollbackFor = {Throwable.class})
    @Override
    public UserDto updateUser(UserSimpleDto dto) throws ObjectNotFoundException {
        User userFromBase = findEntityById(dto.getId());
        userFromBase
                .setFirstName(dto.getFirstName())
                .setSurname(dto.getSurname())
                .setBirthDate(LocalDate.parse(dto.getBirthDate(), DATE_FORMATTER))
                .setAboutMe(dto.getAboutMe());
        return userDtoMapper.fromEntity(updateUser(userFromBase));
    }

    @AuthorizedUser
    @Transactional(rollbackFor = {Throwable.class})
    @Override
    public UserDto updateUserPassword(UserPasswordDto dto) throws ObjectNotFoundException, ServiceException {
        User userFromBase = findEntityById(dto.getId());
        userFromBase.setPassword(dto.getPassword());
        return userDtoMapper.fromEntity(updateUser(userFromBase));
    }

    @AuthorizedUser
    @Transactional(rollbackFor = {Throwable.class})
    @Override
    public void deleteById(Long id) throws ObjectNotFoundException {
        User userFromBase = findEntityById(id);
        userRepository.deleteById(userFromBase.getId());
    }

    @Override
    public UserDto getCurrentAuthorizedUser() throws ObjectNotFoundException, ServiceException {
        return userDtoMapper.fromEntity(
                findEntityById(
                        authorizedUserService.getAuthorizedUserId()));
    }
}

package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.dto.NewUserDto;
import org.senlacourse.social.dto.UserDto;
import org.senlacourse.social.dto.UserPasswordDto;
import org.senlacourse.social.dto.UserSimpleDto;
import org.senlacourse.social.mapstruct.NewUserDtoMapper;
import org.senlacourse.social.mapstruct.UserDtoMapper;
import org.senlacourse.social.repository.RoleRepository;
import org.senlacourse.social.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j
@Transactional
public class UserService extends AbstractService<User> implements IUserService {

    private static final String NOT_DEFINED_FOR_ID = "User not defined for id=";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserDtoMapper userDtoMapper;
    private final NewUserDtoMapper newUserDtoMapper;

    @Override
    protected User findEntityById(Long id) throws ObjectNotFoundException {
        User user = userRepository.findById(id).orElse(null);
        return validateEntityNotNull(user, NOT_DEFINED_FOR_ID + id);
    }

    private User findEntityByLogin(String login) throws ObjectNotFoundException {
        User user = userRepository.findOneByUserLogin(login).orElse(null);
        validateEntityNotNull(user, "User not defined for login=" + login);
        return user;
    }

    @Override
    public Optional<UserDto> findById(Long id) throws ObjectNotFoundException {
        User user = findEntityById(id);
        return Optional.of(userDtoMapper.fromEntity(user));
    }

    @Override
    public Optional<UserDto> findByUserLogin(String userLogin) throws ObjectNotFoundException {
        User user = findEntityByLogin(userLogin);
        return Optional.of(userDtoMapper.fromEntity(user));
    }

    @Override
    public Optional<UserDto> findByUserLoginAndPassword(String userLogin, String password)
            throws ObjectNotFoundException, ServiceException {
        User user = findEntityByLogin(userLogin);
        if (user != null && user.getPassword().equals(password)) {
            return Optional.of(userDtoMapper.fromEntity(user));
        } else {
            ServiceException e = new ServiceException("Incorrect password");
            log.warn(e);
            throw e;
        }
    }

    @Override
    public Optional<UserDto> findByEmail(String email) throws ObjectNotFoundException {
        User user = userRepository.findOneByEmail(email).orElse(null);
        validateEntityNotNull(user, "User not defined for email=" + email);
        return Optional.of(userDtoMapper.fromEntity(user));
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return userDtoMapper.map(userRepository.findAll(pageable));
    }

    @Override
    public Page<UserDto> findAllByFirstNameAndSurname(String firstName, String surname, Pageable pageable) {
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

    @Override
    public UserDto updateUser(UserDto dto) throws ObjectNotFoundException {
        User userFromBase = findEntityById(dto.getId());
        User user = userDtoMapper.toEntity(dto);
        user.setPassword(userFromBase.getPassword());
        return userDtoMapper.fromEntity(updateUser(user));
    }

    @Override
    public UserDto simpleUpdateUser(UserSimpleDto dto) throws ObjectNotFoundException {
        User userFromBase = findEntityById(dto.getId());
        userFromBase
                .setFirstName(dto.getFirstName())
                .setSurname(dto.getSurname())
                .setBirthDate(LocalDate.parse(dto.getBirthDate(), DATE_FORMATTER))
                .setAboutMe(dto.getAboutMe());
        return userDtoMapper.fromEntity(updateUser(userFromBase));
    }

    @Override
    public UserDto updateUserPassword(UserPasswordDto dto) throws ObjectNotFoundException {
        User userFromBase = findEntityById(dto.getId());
        userFromBase.setPassword(dto.getPassword());
        return userDtoMapper.fromEntity(updateUser(userFromBase));
    }

    @Override
    public void deleteById(Long id) throws ObjectNotFoundException {
        User userFromBase = findEntityById(id);
        userRepository.deleteById(userFromBase.getId());
    }
}

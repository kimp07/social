package org.senlacourse.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.domain.Role;
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
public class UserService {

    private static final String USER_ROLE_NAME = "user";
    private static final String NOT_DEFINED_FOR_ID = "User not defined for id=";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserDtoMapper userDtoMapper;
    private final NewUserDtoMapper newUserDtoMapper;

    private Role getUserRole() throws ObjectNotFoundException {
        Role userRole = roleRepository.findByName(USER_ROLE_NAME).orElse(null);
        if (userRole == null) {
            ObjectNotFoundException e = new ObjectNotFoundException("Role not defined for roleName=" + USER_ROLE_NAME);
            log.error(e);
            throw e;
        }
        return userRole;
    }

    private String encodePassword(String password) {
        // TODO: Write encoding password here
        return password;
    }

    private void validateUserNotNull(User user, String exceptionMessage) throws ObjectNotFoundException {
        if (user == null) {
            ObjectNotFoundException e = new ObjectNotFoundException(exceptionMessage);
            log.error(e);
            throw e;
        }
    }

    public Optional<UserDto> findById(Long id) throws ObjectNotFoundException {
        User user = userRepository.findById(id).orElse(null);
        validateUserNotNull(user, NOT_DEFINED_FOR_ID + id);
        return Optional.of(userDtoMapper.fromEntity(user));
    }

    public Optional<UserDto> findByUserLogin(String userLogin) throws ObjectNotFoundException {
        User user = userRepository.findOneByUserLogin(userLogin).orElse(null);
        validateUserNotNull(user, "User not defined for login=" + userLogin);
        return Optional.of(userDtoMapper.fromEntity(user));
    }

    public Optional<UserDto> findByUserLoginAndPassword(String userLogin, String password)
            throws ObjectNotFoundException, ServiceException {
        User user = userRepository.findOneByUserLogin(userLogin).orElse(null);
        validateUserNotNull(user, "User not defined for login=" + userLogin);
        assert user != null;
        if (user.getPassword().equals(encodePassword(password))) {
            return Optional.of(userDtoMapper.fromEntity(user));
        } else {
            ServiceException e = new ServiceException("Incorrect passord");
            log.warn(e);
            throw e;
        }
    }

    public Optional<UserDto> findByEmail(String email) throws ObjectNotFoundException {
        User user = userRepository.findOneByEmail(email).orElse(null);
        validateUserNotNull(user, "User not defined for email=" + email);
        return Optional.of(userDtoMapper.fromEntity(user));
    }

    public Page<UserDto> findAll(Pageable pageable) {
        return userDtoMapper.map(userRepository.findAll(pageable));
    }

    public Page<UserDto> findAllByFirstNameAndSurname(String firstName, String surname, Pageable pageable) {
        return userDtoMapper.map(userRepository.findAllByFirstNameAndSurname(firstName, surname, pageable));
    }

    public UserDto saveUser(NewUserDto dto) throws ObjectNotFoundException {
        User user = newUserDtoMapper.toEntity(dto);
        user.setRole(getUserRole());
        user.setPassword(encodePassword(dto.getPassword()));
        return userDtoMapper.fromEntity(userRepository.save(user));
    }

    private User updateUser(User user) {
        return userRepository.save(user);
    }

    public UserDto updateUser(UserDto dto) throws ObjectNotFoundException {
        User userFromBase = userRepository.findById(dto.getId()).orElse(null);
        validateUserNotNull(userFromBase, NOT_DEFINED_FOR_ID + dto.getId());
        assert userFromBase != null;
        User user = userDtoMapper.toEntity(dto);
        user.setPassword(userFromBase.getPassword());
        return userDtoMapper.fromEntity(updateUser(user));
    }

    public UserDto simpleUpdateUser(UserSimpleDto dto) throws ObjectNotFoundException {
        User userFromBase = userRepository.findById(dto.getId()).orElse(null);
        validateUserNotNull(userFromBase, NOT_DEFINED_FOR_ID + dto.getId());
        assert userFromBase != null;
        userFromBase
                .setFirstName(dto.getFirstName())
                .setSurname(dto.getSurname())
                .setBirthDate(LocalDate.parse(dto.getBirthDate(), DATE_FORMATTER))
                .setAboutMe(dto.getAboutMe());
        return userDtoMapper.fromEntity(updateUser(userFromBase));
    }

    public UserDto updateUserPassword(UserPasswordDto dto) throws ObjectNotFoundException {
        User userFromBase = userRepository.findById(dto.getId()).orElse(null);
        validateUserNotNull(userFromBase, NOT_DEFINED_FOR_ID + dto.getId());
        assert userFromBase != null;
        userFromBase.setPassword(encodePassword(dto.getPassword()));
        return userDtoMapper.fromEntity(updateUser(userFromBase));
    }

    public void deleteUser(UserDto dto) throws ObjectNotFoundException {
        User userFromBase = userRepository.findById(dto.getId()).orElse(null);
        validateUserNotNull(userFromBase, NOT_DEFINED_FOR_ID + dto.getId());
        userRepository.deleteById(dto.getId());
    }
}

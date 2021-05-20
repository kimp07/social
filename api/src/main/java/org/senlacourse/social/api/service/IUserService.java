package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.dto.NewUserDto;
import org.senlacourse.social.dto.UpdateUserDto;
import org.senlacourse.social.dto.UserDto;
import org.senlacourse.social.dto.UserPasswordDto;
import org.senlacourse.social.dto.UserSimpleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IUserService {
    Optional<UserDto> findById(Long id) throws ObjectNotFoundException;

    Optional<UserDto> findByUserLogin(String userLogin) throws ObjectNotFoundException;

    Optional<UserDto> findByUserLoginAndPassword(String userLogin, String password)
            throws ObjectNotFoundException, ServiceException;

    Optional<UserDto> findByEmail(String email) throws ObjectNotFoundException;

    Page<UserDto> findAll(Pageable pageable);

    Page<UserDto> findAllByFirstNameAndSurname(String firstName, String surname, Pageable pageable);

    UserDto saveUser(NewUserDto dto) throws ObjectNotFoundException, ServiceException;

    UserDto updateUser(UserDto dto) throws ObjectNotFoundException;

    UserDto updateUser(UpdateUserDto dto) throws ObjectNotFoundException, ServiceException;

    UserDto simpleUpdateUser(UserSimpleDto dto) throws ObjectNotFoundException;

    UserDto updateUserPassword(UserPasswordDto dto) throws ObjectNotFoundException;

    void deleteById(Long id) throws ObjectNotFoundException;

    UserDto getCurrentAuthorizedUser() throws ObjectNotFoundException;
}

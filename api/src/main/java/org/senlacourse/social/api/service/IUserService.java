package org.senlacourse.social.api.service;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {

    UserDto findById(Long id) throws ObjectNotFoundException;

    UserDto findByUserLogin(String userLogin) throws ObjectNotFoundException;

    UserDto findByUserLoginAndPassword(String userLogin, String password)
            throws ObjectNotFoundException, ServiceException;

    UserDto findByEmail(String email) throws ObjectNotFoundException;

    Page<UserDto> findAll(Pageable pageable);

    Page<UserDto> findAllByFirstNameAndSurname(String firstName, String surname, Pageable pageable);

    UserDto saveUser(NewUserDto dto) throws ObjectNotFoundException, ServiceException;

    UserDto updateUser(UserDto dto) throws ObjectNotFoundException;

    UserDto updateUser(UpdateUserDto dto) throws ObjectNotFoundException, ServiceException;

    UserDto updateUser(UserSimpleDto dto) throws ObjectNotFoundException;

    UserDto updateUserPassword(UserPasswordDto dto) throws ObjectNotFoundException;

    void deleteById(Long id) throws ObjectNotFoundException;

    UserDto getCurrentAuthorizedUser() throws ObjectNotFoundException;
}

package org.senlacourse.social.api.security;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.dto.AuthDto;
import org.senlacourse.social.dto.NewUserDto;
import org.senlacourse.social.dto.RoleDto;
import org.senlacourse.social.dto.UserDto;
import org.senlacourse.social.dto.UserPasswordDto;

public interface IUserSecurityHandlerService {

    RoleDto getRoleUser() throws ObjectNotFoundException;

    void saveUser(NewUserDto newUser) throws ObjectNotFoundException, ServiceException;

    void updateUser(UserDto dto) throws ObjectNotFoundException;

    void updateUserPassword(UserPasswordDto dto) throws ObjectNotFoundException;

    String getUserToken(AuthDto authDto, boolean temporaryToken) throws ObjectNotFoundException;

    String refreshUserToken(boolean temporaryToken) throws ObjectNotFoundException;
}

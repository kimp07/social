package org.senlacourse.social.api.security;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.dto.*;

public interface IUserSecurityHandlerService {

    RoleDto getRoleUser() throws ObjectNotFoundException;

    void saveUser(NewUserDto newUser) throws ObjectNotFoundException, ServiceException;

    void updateUser(UserDto userDto) throws ObjectNotFoundException;

    void updateUserPassword(UserPasswordDto userDto) throws ObjectNotFoundException;

    String getUserToken(AuthDto authDto, boolean temporaryToken) throws ObjectNotFoundException;

    String refreshUserToken(boolean temporaryToken) throws ObjectNotFoundException;
}

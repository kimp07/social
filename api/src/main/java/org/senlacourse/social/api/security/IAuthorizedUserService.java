package org.senlacourse.social.api.security;

import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.dto.IAuthorizedUserDto;

public interface IAuthorizedUserService {

    String getAuthorizedUserLogin() throws ServiceException;

    Long getAuthorizedUserId() throws ServiceException;

    void injectAuthorizedUserId(IAuthorizedUserDto dto);

    Long injectAuthorizedUserId(Long id) throws ServiceException;
}

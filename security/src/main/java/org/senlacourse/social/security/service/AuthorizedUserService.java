package org.senlacourse.social.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.security.IAuthorizedUserService;
import org.senlacourse.social.dto.IAuthorizedUserDto;
import org.senlacourse.social.security.ApplicationUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j
public class AuthorizedUserService implements IAuthorizedUserService {

    private ApplicationUserDetails getUserDetails() throws ServiceException {
        Authentication authToken = SecurityContextHolder.getContext().getAuthentication();
        ApplicationUserDetails userDetails = (ApplicationUserDetails) authToken.getPrincipal();
        if (userDetails == null) {
            ServiceException e = new ServiceException("Authorized user not found");
            log.error(e);
            throw e;
        } else {
            return userDetails;
        }
    }

    @Override
    public String getAuthorizedUserLogin() throws ServiceException {
        return getUserDetails().getUsername();
    }

    @Override
    public Long getAuthorizedUserId() throws ServiceException {
        return getUserDetails().getUserId();
    }

    @Override
    public void injectAuthorizedUserId(IAuthorizedUserDto dto) throws ServiceException {
        if (dto.getAuthorizedUserId() == null || dto.getAuthorizedUserId().equals(0L)) {
            dto.setAuthorizedUserId(getAuthorizedUserId());
        }
    }

}

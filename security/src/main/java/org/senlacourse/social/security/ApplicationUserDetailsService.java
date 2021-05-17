package org.senlacourse.social.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ApplicationException;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.domain.User;
import org.senlacourse.social.mapstruct.UserDtoMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j
public class ApplicationUserDetailsService implements UserDetailsService {

    private final IUserService userService;
    private final UserDtoMapper userDtoMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) {
        User user;
        try {
            user = userDtoMapper.toEntity(userService.findByUserLogin(userName).orElse(null));
        } catch (ObjectNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(e);
        }
        return ApplicationUserDetails.createFromUser(user);
    }

}

package org.senlacourse.social.api.security;

import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface IJwtProvider {

    String createToken(String userName, String userPassword, boolean temporaryToken);

    UserDetails getUserDetailsFromToken(String token) throws ObjectNotFoundException;

    Optional<UserDetails> getUserDetails(ServletRequest request);

    String getTokenFromRequest(HttpServletRequest request);

    boolean validateToken(String authToken);
}

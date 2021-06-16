package org.senlacourse.social.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ApplicationException;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.security.IJwtProvider;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.dto.UserDto;
import org.senlacourse.social.mapstruct.UserDtoMapper;
import org.senlacourse.social.security.ApplicationUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j
@RequiredArgsConstructor
public class JwtProvider extends AbstractUserDetailsAuthenticationProvider implements IJwtProvider {

    private static final String AUTHORIZATION = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";

    private static final String AUTHORITIES_KEY = "auth";

    private final IUserService userService;
    private final UserDtoMapper userDtoMapper;

    @Value("${application.jwt.term-days:5}")
    private Long jwtTermDays;
    @Value("${application.jwt.term-days-temporary:1}")
    private Long jwtTermDaysTemporary;
    @Value("${application.jwt.base-secret}")
    private String baseSecret;
    @Value("${application.jwt.temporary-role:ROLE_TEMPORARY}")
    private String temporaryRole;

    private Key key;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        // empty
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        Object token = authentication.getCredentials();
        try {
            return getUserDetailsFromToken(token.toString());
        } catch (ObjectNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(e);
        }
    }

    private Key getSigningKey() {
        if (baseSecret == null) {
            ApplicationException e =
                    new ApplicationException("Base secret not defined. Please define property social.jwt.baseSecret");
            log.error(e.getMessage(), e);
            throw e;
        }
        if (key == null) {
            key = new SecretKeySpec(baseSecret.getBytes(), SignatureAlgorithm.HS512.getJcaName());
        }
        return key;
    }

    private String getAuthorities(Authentication authentication, boolean temporaryToken) {
        if (!temporaryToken) {
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));
        } else {
            return temporaryRole;
        }
    }

    private String createToken(Authentication authentication, boolean temporaryToken) {
        String authorities = getAuthorities(authentication, temporaryToken);
        Date validity;
        validity = Date.from(
                LocalDate
                        .now().plusDays(temporaryToken ? jwtTermDaysTemporary : jwtTermDays)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(SignatureAlgorithm.HS512, getSigningKey())
                .setExpiration(validity)
                .compact();
    }

    @Override
    public String createToken(String userName, String userPassword, boolean temporaryToken) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userName, userPassword);
        return createToken(authenticationToken, temporaryToken);
    }

    @Override
    public UserDetails getUserDetailsFromToken(String token) throws ObjectNotFoundException {
        Claims claims = getClaimsFromTocken(token);
        String userName = claims.getSubject();
        UserDto userDto = userService.findByUserLogin(userName);
        if (userDto == null) {
            throw new ApplicationException("User not found for userName " + userName);
        }
        if (claims.containsKey(AUTHORITIES_KEY) && claims.get(AUTHORITIES_KEY).equals(temporaryRole)) {
            return ApplicationUserDetails
                    .createFromUser(
                            userDtoMapper.toEntity(userDto), true, temporaryRole);
        } else {
            return ApplicationUserDetails
                    .createFromUser(
                            userDtoMapper.toEntity(userDto));
        }
    }

    private Claims getClaimsFromTocken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Optional<UserDetails> getUserDetails(ServletRequest request) {
        UserDetails userDetails = null;
        String token = getTokenFromRequest((HttpServletRequest) request);
        if (token != null && validateToken(token)) {
            try {
                userDetails = getUserDetailsFromToken(token);
            } catch (ObjectNotFoundException e) {
                log.error(e.getMessage(), e);
                throw new ApplicationException(e);
            }
        }
        return Optional.ofNullable(userDetails);
    }

    @Override
    public String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(token) && token.startsWith(JWT_PREFIX)) {
            return token.substring(JWT_PREFIX.length());
        }
        return null;
    }

    @Override
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(authToken);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature.", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token.", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT token compact of handler are invalid.", e);
        }
        return false;
    }
}
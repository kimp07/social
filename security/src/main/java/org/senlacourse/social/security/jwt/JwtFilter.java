package org.senlacourse.social.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Log4j
public class JwtFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;

    private static final String AUTHORIZATION = "Authorization"; // Bearer token header
    private static final String JWT_PREFIX = "Bearer ";

    @Override
    public void doFilter(
            ServletRequest request, ServletResponse response, FilterChain filterChain
    ) throws IOException, ServletException {
        log.info("Starting filter");
        String token = getTokenFromRequest((HttpServletRequest) request);
        if (token != null && jwtProvider.validateToken(token)) {
            UserDetails customUserDetails = jwtProvider.getUserDetails(request).orElse(null);
            if (customUserDetails != null) {
                UsernamePasswordAuthenticationToken auth
                        = new UsernamePasswordAuthenticationToken(
                        customUserDetails,
                        jwtProvider
                                .getTokenFromRequest((HttpServletRequest) request),
                        customUserDetails.getAuthorities());
                SecurityContextHolder.getContext()
                        .setAuthentication(auth);
            }
            filterChain.doFilter(request, response);

        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(token) && token.startsWith(JWT_PREFIX)) {
            return token.substring(JWT_PREFIX.length());
        }
        return null;
    }
}

package org.senlacourse.social.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.security.IAuthorizedUserService;
import org.senlacourse.social.api.security.IUserSecurityHandlerService;
import org.senlacourse.social.api.service.IRoleService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.dto.*;
import org.senlacourse.social.security.jwt.JwtProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j
public class UserSecurityHandlerService implements IUserSecurityHandlerService {

    private static final String ROLE_USER = "USER";

    private final IUserService userService;
    private final IRoleService roleService;
    private final JwtProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final IAuthorizedUserService authorizedUserService;

    private RoleDto roleUser;

    private RoleDto getRoleByNameUser() {
        RoleDto role = null;
        try {
            role = roleService.findByName(ROLE_USER);
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage(), e);
        }
        if (role == null) {
            role = roleService.saveRole(
                    new NewRoleDto()
                            .setRoleName(ROLE_USER));
        }
        return role;
    }

    @Override
    public RoleDto getRoleUser() throws ObjectNotFoundException {
        if (roleUser == null) {
            roleUser = getRoleByNameUser();
        }
        return roleUser;
    }

    @Override
    public void saveUser(NewUserDto newUser) throws ObjectNotFoundException, ServiceException {
        if (newUser.getRoleId() == null) {
            newUser.setRoleId(getRoleUser().getId());
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userService.saveUser(newUser);
    }

    private UserDto getUserFromBase(Long id) throws ObjectNotFoundException {
        return userService.findById(id);
    }

    @Override
    public void updateUser(UserDto userDto) throws ObjectNotFoundException {
        authorizedUserService.injectAuthorizedUserId(userDto);
        userDto.setPassword(
                getUserFromBase(
                        userDto.getId()
                ).getPassword());
        userService.updateUser(userDto);
    }

    @Override
    public void updateUserPassword(UserPasswordDto userDto) throws ObjectNotFoundException {
        authorizedUserService.injectAuthorizedUserId(userDto);
        UserDto userFromBase = getUserFromBase(userDto.getId());
        userFromBase.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userService.updateUser(userFromBase);
    }

    private String getToken(String userName, boolean temporaryToken) throws ObjectNotFoundException {
        UserDto user = userService.findByUserLogin(userName);
        if (user != null) {
            return tokenProvider.createToken(
                    userName,
                    user.getPassword(),
                    temporaryToken);
        } else {
            ObjectNotFoundException e = new ObjectNotFoundException("User not defined for name=" + userName);
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public String getUserToken(AuthDto authDto, boolean temporaryToken) throws ObjectNotFoundException {
        return getToken(authDto.getLogin(), temporaryToken);
    }

    @Override
    public String refreshUserToken(boolean temporaryToken) throws ObjectNotFoundException {
        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        ApplicationUserDetails userDetails = (ApplicationUserDetails) auth.getPrincipal();
        return getToken(userDetails.getUsername(), temporaryToken);
    }

}

package org.senlacourse.social.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.senlacourse.social.api.security.IUserSecurityHandlerService;
import org.senlacourse.social.api.service.IRoleService;
import org.senlacourse.social.api.service.IUserService;
import org.senlacourse.social.dto.AuthDto;
import org.senlacourse.social.dto.EmailDto;
import org.senlacourse.social.dto.NewRoleDto;
import org.senlacourse.social.dto.NewUserDto;
import org.senlacourse.social.dto.RoleDto;
import org.senlacourse.social.dto.UserDto;
import org.senlacourse.social.dto.UserPasswordDto;
import org.senlacourse.social.security.jwt.JwtProvider;
import org.senlacourse.social.security.service.AuthorizedUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;
    @Value("${application.host}")
    private String applicationHost;
    @Value("${application.restore-password-redirect}")
    private String restorePasswordRedirect;

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

    @AuthorizedUser
    @Override
    public void updateUser(UserDto dto) throws ObjectNotFoundException {
        dto.setPassword(
                getUserFromBase(
                        dto.getId()
                ).getPassword());
        userService.updateUser(dto);
    }

    @AuthorizedUser
    @Override
    public void updateUserPassword(UserPasswordDto dto) throws ObjectNotFoundException {
        UserDto userFromBase = getUserFromBase(dto.getId());
        userFromBase.setPassword(passwordEncoder.encode(dto.getPassword()));
        userService.updateUser(userFromBase);
    }

    private String getToken(String userName, boolean temporaryToken) throws ObjectNotFoundException {
        UserDto user = userService.findByUserLogin(userName);
        return tokenProvider.createToken(
                userName,
                user.getPassword(),
                temporaryToken);

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

    private String getTemporaryToken(String email) throws ObjectNotFoundException {
        return getToken(
                userService
                        .findByEmail(email)
                        .getLogin(),
                true);
    }

    private void sendRestoreEmail(String email, String temporaryToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(email);
        message.setSubject("Password recovery");
        String text = "For recovering password follow link\n " +
                "Link is avaliable in 24 hours\n" +
                restorePasswordRedirect + "?token=" + temporaryToken;
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public String restoreAssess(EmailDto dto) throws ObjectNotFoundException {
        String temporaryToken = getTemporaryToken(dto.getEmail());
        sendRestoreEmail(dto.getEmail(), temporaryToken);
        return temporaryToken;
    }

}

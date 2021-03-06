package org.senlacourse.social.security;

import org.senlacourse.social.domain.Role;
import org.senlacourse.social.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class ApplicationUserDetails implements UserDetails {

    private static final long serialVersionUID = -1497528132480261808L;

    private Long userId;
    private String userName;
    private String password;
    private Collection<? extends GrantedAuthority> grantedAuthoritys;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public static ApplicationUserDetails createFromUser(User user, boolean temporaryAuthority, String temporaryRole) {
        ApplicationUserDetails userDetails = new ApplicationUserDetails();
        userDetails.userId = user.getId();
        userDetails.userName = user.getLogin();
        userDetails.password = user.getPassword();
        if (!temporaryAuthority) {
            userDetails.grantedAuthoritys = Collections.singletonList(
                    new SimpleGrantedAuthority(
                            userDetails.getValidatedRoleName(user.getRole())
                    ));
        } else {
            userDetails.grantedAuthoritys = Collections.singletonList(
                    new SimpleGrantedAuthority(temporaryRole));
        }
        userDetails.accountNonExpired = user.getNonExpired();
        userDetails.accountNonLocked = user.getNonLocked();
        userDetails.credentialsNonExpired = user.getCredentialsNonExpired();
        userDetails.enabled = user.getEnabled();

        return userDetails;
    }

    public static ApplicationUserDetails createFromUser(User user) {
        return createFromUser(user, false, "");
    }

    private String getValidatedRoleName(Role role) {
        String roleName = role.getRoleName();
        if (roleName.startsWith("ROLE_")) {
            return roleName;
        }
        return "ROLE_" + roleName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthoritys;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Long getUserId() {
        return userId;
    }
}
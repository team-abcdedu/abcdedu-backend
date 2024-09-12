package com.abcdedu_backend.global.security;

import com.abcdedu_backend.member.entity.MemberRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class LoginUserDetails implements UserDetails {
    private static final String ROLE_PREFIX = "ROLE_";
    private final Long userId;
    private final MemberRole role;

    public LoginUserDetails(Long userId, MemberRole role) {
        this.userId = userId;
        this.role = role;
    }


    public Long getLoginUserId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(ROLE_PREFIX + role.name()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }
}

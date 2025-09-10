package com.minhaCarteira.write.infra.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.minhaCarteira.write.domain.usuario.Usuario;

import java.util.Collection;

public class UserAuthenticated implements UserDetails {

    private final Usuario user;

    public UserAuthenticated(Usuario user){this.user = user;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.getRoleList().stream().map(item -> new SimpleGrantedAuthority(item.name())).toList();
    }

    @Override
    public String getPassword() {
        return this.user.getSenha();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

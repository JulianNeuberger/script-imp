package de.fsmpi.model.user;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Julian on 31.01.2017.
 */
public enum UserRole implements GrantedAuthority {

    USER, ADMIN;

    String role;

    UserRole() {
        this.role = this.toString();
    }


    @Override
    public String getAuthority() {
        return this.role;
    }
}

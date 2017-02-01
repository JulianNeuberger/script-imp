package de.fsmpi.model.user;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Julian on 31.01.2017.
 */
public enum UserAuthority implements GrantedAuthority {
    EDIT_OPTIONS, DO_PRINT, VIEW_DOCUMENTS, PRINT, REQUEST_PRINT;

    String role;

    UserAuthority() {
        this.role = this.toString();
    }


    @Override
    public String getAuthority() {
        return this.role;
    }
}

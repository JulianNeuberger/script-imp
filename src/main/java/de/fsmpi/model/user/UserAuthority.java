package de.fsmpi.model.user;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Julian on 31.01.2017.
 */
public enum UserAuthority implements GrantedAuthority {
	EDIT_OPTIONS, // the closest we get to an admin
	DO_PRINT, MANAGE_DOCUMENTS, MANAGE_USERS, // elevated users --> mods
	REQUEST_PRINT, VIEW_DOCUMENTS, VIEW_PDF; // normal peasant class users

	String role;

	UserAuthority() {
		this.role = this.toString();
	}


	@Override
	public String getAuthority() {
		return this.role;
	}
}

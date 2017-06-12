package de.fsmpi.service;

import de.fsmpi.model.user.User;
import de.fsmpi.model.user.UserAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;

/**
 * Created by Killerzelle on 01.02.2017.
 */
public interface UserService extends UserDetailsService {
	User getById(String userName);

	Iterable<User> getAll();

    User register(User user);

	User updateProfile(User user, String password, String firstName, String lastName, String email);

	User updateAuthorities(String userName, Set<UserAuthority> authorities);

	boolean currentUserAllowedToPrint();
}

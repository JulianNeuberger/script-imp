package de.fsmpi.service;

import de.fsmpi.model.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by Killerzelle on 01.02.2017.
 */
public interface UserService extends UserDetailsService {
    User register(User user);
}

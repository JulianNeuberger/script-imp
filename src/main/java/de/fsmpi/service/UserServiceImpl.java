package de.fsmpi.service;

import de.fsmpi.model.user.User;
import de.fsmpi.model.user.UserAuthority;
import de.fsmpi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final NotificationService notificationService;

	@Autowired
	private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
						   NotificationService notificationService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(this.userRepository.exists(username)) {
            return this.userRepository.findOne(username);
        } else {
            throw new UsernameNotFoundException(
                    MessageFormat.format("User with username {0} does not exist.", username)
            );
        }
    }

    @Override
    public User register(User user) {
        if(user.getUserAuthorities() == null) {
            user.setUserAuthorities(new HashSet<>());
        }
        user.getUserAuthorities().add(UserAuthority.VIEW_DOCUMENTS);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user = this.userRepository.save(user);
		notificationService.createNotification(user, "notification.user_profile", "/user/show/self");
		return user;
	}

    @Override
	public User updateProfile(User user, String password, String firstName, String lastName, String email) {
    	if(password.length() > 0) {
			user.setPassword(password);
    	}
    	if(firstName.length() > 0) {
    		user.setFirstName(firstName);
		}
		if(lastName.length() > 0) {
    		user.setLastName(lastName);
		}
		if(email.length() > 0) {
		}
		return userRepository.save(user);
	}

	@Override
	public boolean currentUserAllowedToPrint() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return principal instanceof User && ((User) principal).getAuthorities().contains(UserAuthority.DO_PRINT);
	}
}

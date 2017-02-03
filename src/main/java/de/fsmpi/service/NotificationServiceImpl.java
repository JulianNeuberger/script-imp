package de.fsmpi.service;

import de.fsmpi.model.user.*;
import de.fsmpi.repository.NotificationRepository;
import de.fsmpi.repository.RoleNotificationRepository;
import de.fsmpi.repository.UserNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final UserNotificationRepository userNotificationRepository;
    private final RoleNotificationRepository roleNotificationRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(UserNotificationRepository userNotificationRepository,
                                   RoleNotificationRepository roleNotificationRepository,
                                   NotificationRepository notificationRepository) {
        this.userNotificationRepository = userNotificationRepository;
        this.roleNotificationRepository = roleNotificationRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public boolean hasNewNotificationsForUser(User user) {
        return this.getNewNotificationsForUser(user).size() > 0;
    }

    /**
     * Gets all notifactions relevant to that user, i.e. the ones for his roles, too
     * @param user the user in question
     * @return a list of notifications relevant for that user
     */
    @Override
    public Set<Notification> getNotificationsForUser(User user) {
        Set<Notification> notifications = this.userNotificationRepository.findByUser(user);
        for (UserAuthority userAuthority : user.getUserAuthorities()) {
            notifications.addAll(this.getNotificationsForRole(userAuthority));
        }
        return notifications;
    }

    @Override
    public Set<Notification> getNewNotificationsForUser(User user) {
        Set<Notification> notifications = this.userNotificationRepository.findByUserAndRead(user, false);
        for (UserAuthority userAuthority : user.getUserAuthorities()) {
            notifications.addAll(this.getNewNotificationsForRole(userAuthority));
        }
        return notifications;
    }

    @Override
    public Set<Notification> getOldNotificationsForUser(User user) {
        Set<Notification> notifications = this.userNotificationRepository.findByUserAndRead(user, true);
        for (UserAuthority userAuthority : user.getUserAuthorities()) {
            notifications.addAll(this.getOldNotificationsForAuthority(userAuthority));
        }
        return notifications;
    }

    @Override
    public Set<Notification> getOldNotificationsForAuthority(UserAuthority userAuthority) {
        return this.roleNotificationRepository.findByUserAuthorityAndRead(userAuthority, true);
    }

    @Override
    public boolean hasNewNotificationsForRole(UserAuthority authority) {
        return this.roleNotificationRepository.findByUserAuthority(authority).size() > 0;
    }

    @Override
    public Set<Notification> getNotificationsForRole(UserAuthority userAuthority) {
        return this.roleNotificationRepository.findByUserAuthority(userAuthority);
    }

    @Override
    public Set<Notification> getNewNotificationsForRole(UserAuthority userAuthority) {
        return this.roleNotificationRepository.findByUserAuthorityAndRead(userAuthority, false);
    }

    @Override
    public Notification createNotification(User user, String message, String target) {
        UserNotification notification = new UserNotification();
        notification.setUser(user);
        this.createHelper(notification, message, target);
        return this.notificationRepository.save(notification);
    }

    @Override
    public Notification createNotification(UserAuthority role, String message, String target) {
        RoleNotification notification = new RoleNotification();
        notification.setUserAuthority(role);
        this.createHelper(notification, message, target);
        return this.notificationRepository.save(notification);
    }

    @Override
    public void mark(Notification notification, boolean read) {
        notification.setRead(read);
        this.notificationRepository.save(notification);
    }

    @Override
    public void markAll(User user, boolean read) {
        for (Notification notification : this.getNotificationsForUser(user)) {
            this.mark(notification, read);
        }
    }

    private void createHelper(Notification notification, String message, String target) {
        notification.setMessage(message);
        notification.setTarget(target);
    }
}

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
        for (UserRole userRole : user.getUserRoles()) {
            notifications.addAll(this.getNotificationsForRole(userRole));
        }
        return notifications;
    }

    @Override
    public Set<Notification> getNewNotificationsForUser(User user) {
        Set<Notification> notifications = this.userNotificationRepository.findByUserAndRead(user, false);
        for (UserRole userRole : user.getUserRoles()) {
            notifications.addAll(this.getNewNotificationsForRole(userRole));
        }
        return notifications;
    }

    @Override
    public boolean hasNewNotificationsForRole(UserRole role) {
        return this.roleNotificationRepository.findByRole(role).size() > 0;
    }

    @Override
    public Set<Notification> getNotificationsForRole(UserRole role) {
        return this.roleNotificationRepository.findByRole(role);
    }

    @Override
    public Set<Notification> getNewNotificationsForRole(UserRole role) {
        return this.roleNotificationRepository.findByRoleAndRead(role, false);
    }

    @Override
    public Notification createNotification(User user, String message, String target) {
        UserNotification notification = new UserNotification();
        notification.setUser(user);
        this.createHelper(notification, message, target);
        return this.notificationRepository.save(notification);
    }

    @Override
    public Notification createNotification(UserRole role, String message, String target) {
        RoleNotification notification = new RoleNotification();
        notification.setRole(role);
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

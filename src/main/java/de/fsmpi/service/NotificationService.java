package de.fsmpi.service;

import de.fsmpi.model.user.Notification;
import de.fsmpi.model.user.User;
import de.fsmpi.model.user.UserRole;

import java.util.Set;

public interface NotificationService {

    boolean hasNewNotificationsForUser(User user);

    Set<Notification> getNotificationsForUser(User user);

    Set<Notification> getNewNotificationsForUser(User user);

    boolean hasNewNotificationsForRole(UserRole role);

    Set<Notification> getNotificationsForRole(UserRole role);

    Set<Notification> getNewNotificationsForRole(UserRole role);

    Notification createNotification(User user, String message, String target);

    Notification createNotification(UserRole role, String message, String target);

    void mark(Notification notification, boolean read);

    void markAll(User user, boolean read);
}

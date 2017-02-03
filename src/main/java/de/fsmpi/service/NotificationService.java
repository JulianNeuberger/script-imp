package de.fsmpi.service;

import de.fsmpi.model.user.Notification;
import de.fsmpi.model.user.User;
import de.fsmpi.model.user.UserAuthority;

import java.util.Set;

public interface NotificationService {

    boolean hasNewNotificationsForUser(User user);

    Set<Notification> getNotificationsForUser(User user);

    Set<Notification> getNewNotificationsForUser(User user);

	Set<Notification> getOldNotificationsForUser(User user);

	Set<Notification> getOldNotificationsForAuthority(UserAuthority userAuthority);

	boolean hasNewNotificationsForRole(UserAuthority role);

    Set<Notification> getNotificationsForRole(UserAuthority role);

    Set<Notification> getNewNotificationsForRole(UserAuthority role);

    Notification createNotification(User user, String message, String target);

    Notification createNotification(UserAuthority role, String message, String target);

    void mark(Notification notification, boolean read);

    void markAll(User user, boolean read);
}

package de.fsmpi.repository;

import de.fsmpi.model.user.Notification;
import de.fsmpi.model.user.User;
import de.fsmpi.model.user.UserNotification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserNotificationRepository extends CrudRepository<UserNotification, Long> {
    Set<Notification> findByUser(User user);

    Set<Notification> findByUserAndRead(User user, Boolean read);
}

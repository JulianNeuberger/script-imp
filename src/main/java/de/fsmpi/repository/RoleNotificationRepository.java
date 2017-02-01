package de.fsmpi.repository;

import de.fsmpi.model.user.Notification;
import de.fsmpi.model.user.RoleNotification;
import de.fsmpi.model.user.UserAuthority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleNotificationRepository extends CrudRepository<RoleNotification, Long> {
    Set<Notification> findByRole(UserAuthority role);

    Set<Notification> findByRoleAndRead(UserAuthority role, Boolean read);
}

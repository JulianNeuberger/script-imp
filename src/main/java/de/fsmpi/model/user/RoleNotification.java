package de.fsmpi.model.user;

import javax.persistence.*;

/**
 * Created by Julian on 01.02.2017.
 */
@Entity
@DiscriminatorValue("role")
public class RoleNotification extends Notification {

    @Column
    @Enumerated(EnumType.STRING)
    private UserAuthority role;

    public UserAuthority getRole() {
        return role;
    }

    public void setRole(UserAuthority role) {
        this.role = role;
    }
}

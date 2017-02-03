package de.fsmpi.model.user;

import javax.persistence.*;

/**
 * Created by Julian on 01.02.2017.
 */
@Entity
@DiscriminatorValue("userAuthority")
public class RoleNotification extends Notification {

    @Column
    @Enumerated(EnumType.STRING)
    private UserAuthority userAuthority;

    public UserAuthority getUserAuthority() {
        return userAuthority;
    }

    public void setUserAuthority(UserAuthority userAuthority) {
        this.userAuthority = userAuthority;
    }
}

package de.fsmpi.model.user;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by Julian on 01.02.2017.
 */
@Entity
@DiscriminatorValue("user")
public class UserNotification extends Notification {

    @ManyToOne
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

package de.fsmpi.model.user;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Table
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Notification {

    @Id
    @Column
    @GeneratedValue
    protected Long id;

    @Column
    private String message;

    @Column
    private String target;

    @Column(name = "`read`")
    private Boolean read = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }
}

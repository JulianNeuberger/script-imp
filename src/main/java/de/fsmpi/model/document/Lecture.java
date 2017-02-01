package de.fsmpi.model.document;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

/**
 * Created by Julian on 07.12.2016.
 */
@Entity
@Table
public class Lecture {

    @Id
    @Column
    @GeneratedValue
    protected Long id;

    @Column
    protected String name;

    @Column
    protected Field field;

    @ManyToMany(mappedBy = "lectures")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    protected Set<Lecturer> lecturers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Set<Lecturer> getLecturers() {
        return lecturers;
    }

    public void setLecturers(Set<Lecturer> lecturers) {
        this.lecturers = lecturers;
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "name='" + name + '\'' +
                ", field=" + field +
                '}';
    }
}

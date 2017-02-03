package de.fsmpi.model.document;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Julian on 07.12.2016.
 */
@Entity
@Table
public class Lecturer {

	@Id
	@Column
	@GeneratedValue
	protected Long id;

	@Column
	protected String name;

	@JoinTable
	@ManyToMany
	@Cascade(org.hibernate.annotations.CascadeType.DELETE)
	private Set<Lecture> lectures;

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

	public Set<Lecture> getLectures() {
		return lectures;
	}

	public void setLectures(Set<Lecture> lectures) {
		this.lectures = lectures;
	}

	@Override
	public String toString() {
		return "Lecturer{" +
				"key='" + name + '\'' +
				'}';
	}
}

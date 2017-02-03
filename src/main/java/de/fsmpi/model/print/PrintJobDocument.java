package de.fsmpi.model.print;

import de.fsmpi.model.document.Document;

import javax.persistence.*;

@Entity
@Table
public class PrintJobDocument {

	@Id
	@Column
	@GeneratedValue
	private Long id;

	@ManyToOne
	private Document document;

	@Column
	private Integer count;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PrintJobDocument that = (PrintJobDocument) o;

		return id != null ? id.equals(that.id) : that.id == null;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}

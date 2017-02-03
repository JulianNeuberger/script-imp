package de.fsmpi.misc;

import de.fsmpi.model.document.Document;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table
public class Cart {

	@Id
	@Column
	@GeneratedValue
	private Long id;

	@JoinTable
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Document> documents = new ArrayList<>();

    public List<Document> getDocuments() {
        return Collections.unmodifiableList(documents);
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Transient
    public void addDocumentToCart(Document document) {
        this.documents.add(document);
    }

    @Transient
    public void removeDocumentFromCart(Document document) {
        this.documents.remove(document);
    }

    @Transient
    public void clear() {
        this.documents = new ArrayList<>();
    }

    @Transient
    public int getItemCount() {
        return this.documents.size();
    }
}

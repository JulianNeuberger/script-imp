package de.fsmpi.misc;

import de.fsmpi.model.document.Document;
import de.fsmpi.model.print.PrintJobDocument;

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
	private List<PrintJobDocument> documents = new ArrayList<>();

    public List<PrintJobDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<PrintJobDocument> documents) {
        this.documents = documents;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Transient
    public void addItemToCart(PrintJobDocument printJobDocument) {
    	this.documents.add(printJobDocument);
    }

    @Transient
    public void removeItemFromCart(PrintJobDocument document) {
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

    @Transient
    public boolean hasDocument(Document document) {
		for (PrintJobDocument printJobDocument : documents) {
			if(printJobDocument.getDocument().equals(document)) {
				return true;
			}
		}
		return false;
	}

	@Transient
	public PrintJobDocument getItemForId(Long id) {
		for (PrintJobDocument document : documents) {
			if(document.getId().equals(id)) {
				return document;
			}
		}
		return null;
	}

	@Transient
	public PrintJobDocument getItemForDocument(Document document) {
		for (PrintJobDocument printJobDocument : documents) {
			if(printJobDocument.getDocument().equals(document)) {
				return printJobDocument;
			}
		}
		return null;
	}

	@Transient
	public void removeItemFromCartForDocument(Document document) {
    	for (PrintJobDocument printJobDocument : documents) {
			if(printJobDocument.getDocument().equals(document)) {
				removeItemFromCart(printJobDocument);
			}
		}
	}
}

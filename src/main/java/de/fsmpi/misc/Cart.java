package de.fsmpi.misc;

import de.fsmpi.model.document.Document;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Julian on 27.01.2017.
 */
@Component
@Scope("session")
public class Cart {

    private List<Document> documents = new ArrayList<>();

    public List<Document> getDocuments() {
        return Collections.unmodifiableList(documents);
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public void addDocumentToCart(Document document) {
        this.documents.add(document);
    }

    public void removeDocumentFromCart(Document document) {
        this.documents.remove(document);
    }

    public void clear() {
        this.documents = new ArrayList<>();
    }
}

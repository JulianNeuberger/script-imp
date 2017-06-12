package de.fsmpi.service;

import de.fsmpi.model.document.Document;
import de.fsmpi.model.print.PrintJobDocument;

import java.util.List;

public interface PrintJobDocumentService {

	PrintJobDocument createPrintJobDocument(Document document);

	PrintJobDocument createPrintJobDocument(Document document, Integer count);

	List<PrintJobDocument> createPrintJobDocuments(Iterable<Document> documents);

	List<PrintJobDocument> createPrintJobDocuments(Iterable<Document> documents, Integer count);

	PrintJobDocument findOne(Long id);

	PrintJobDocument updateCopyCount(PrintJobDocument printJobDocument, int copyCount);

	void removeAll(Iterable<PrintJobDocument> printJobDocuments);
}

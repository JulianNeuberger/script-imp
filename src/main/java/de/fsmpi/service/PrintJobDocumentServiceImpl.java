package de.fsmpi.service;

import de.fsmpi.model.document.Document;
import de.fsmpi.model.print.PrintJob;
import de.fsmpi.model.print.PrintJobDocument;
import de.fsmpi.repository.PrintJobDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrintJobDocumentServiceImpl implements PrintJobDocumentService {
	private final PrintJobDocumentRepository printJobDocumentRepository;

	@Autowired
	public PrintJobDocumentServiceImpl(PrintJobDocumentRepository printJobDocumentRepository) {
		this.printJobDocumentRepository = printJobDocumentRepository;
	}


	@Override
	public PrintJobDocument createPrintJobDocument(Document document) {
		return this.createPrintJobDocument(document, 1);
	}

	@Override
	public PrintJobDocument createPrintJobDocument(Document document, Integer count) {
		PrintJobDocument printJobDocument = new PrintJobDocument();
		printJobDocument.setCount(count);
		printJobDocument.setDocument(document);
		return printJobDocumentRepository.save(printJobDocument);
	}

	@Override
	public List<PrintJobDocument> createPrintJobDocuments(Iterable<Document> documents) {
		return createPrintJobDocuments(documents, 1);
	}

	@Override
	public List<PrintJobDocument> createPrintJobDocuments(Iterable<Document> documents, Integer count) {
		List<PrintJobDocument> printJobDocuments = new ArrayList<>();
		for (Document document : documents) {
			printJobDocuments.add(createPrintJobDocument(document, count));
		}
		return printJobDocuments;
	}

	@Override
	public PrintJobDocument findOne(Long id) {
		return this.printJobDocumentRepository.findOne(id);
	}

	@Override
	public PrintJobDocument updateCopyCount(PrintJobDocument printJobDocument, int copyCount) {
		// TODO: validation, e.g. no negative counts
		printJobDocument.setCount(copyCount);
		return printJobDocumentRepository.save(printJobDocument);
	}

	@Override
	public void removeAll(Iterable<PrintJobDocument> printJobDocuments) {
		printJobDocumentRepository.delete(printJobDocuments);
	}
}

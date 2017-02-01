package de.fsmpi.service;

import de.fsmpi.model.document.Document;
import de.fsmpi.model.print.PrintJob;

import javax.print.PrintService;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface PrintingService {

    PrintJob printDocument(Document document);

    PrintJob printDocuments(Collection<Document> documents);

    void setDefaultPrinter(String name);

    Set<PrintService> findRegisteredPrinters();

    boolean isRegisteredPrinter(String name);

    PrintService getPrinterForName(String name);

    PrintJob tryPrint(Collection<Document> documents);

    PrintJob tryPrint(Document document);

    PrintService getDefaultPrinter();

    PrintJob doPrintJob(PrintJob job);

    List<PrintJob> doPrintJobs(Iterable<PrintJob> jobs);
}

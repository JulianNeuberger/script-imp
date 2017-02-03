package de.fsmpi.service;

import de.fsmpi.model.document.Document;
import de.fsmpi.model.print.PrintJob;
import de.fsmpi.model.print.PrintJobDocument;

import javax.print.PrintService;
import java.util.List;
import java.util.Set;

public interface PrintingService {

	PrintJob printDocuments(PrintJob printJob);

	void setDefaultPrinter(String name);

	Set<PrintService> findRegisteredPrinters();

	boolean isRegisteredPrinter(String name);

	PrintService getPrinterForName(String name);

	PrintService getDefaultPrinter();

	PrintJob doPrintJob(PrintJob job);

	PrintJob doPrintJob(Long jobId);

	List<PrintJob> doPrintJobs(Iterable<PrintJob> jobs);
}

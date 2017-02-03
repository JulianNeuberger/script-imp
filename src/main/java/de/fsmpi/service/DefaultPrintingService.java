package de.fsmpi.service;

import de.fsmpi.model.document.Document;
import de.fsmpi.model.option.Option;
import de.fsmpi.model.print.PrintJob;
import de.fsmpi.model.print.PrintJobDocument;
import de.fsmpi.model.print.PrintStatus;
import de.fsmpi.model.user.UserAuthority;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;

@Service
public class DefaultPrintingService implements PrintingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPrintingService.class);

	private final OptionService optionService;
	private final PrintJobManager printJobManager;
	private final NotificationService notificationService;
	private final UserService userService;

	@Autowired
	public DefaultPrintingService(OptionService optionService,
								  NotificationService notificationService,
								  UserService userService,
								  PrintJobManager printJobManager) {
		this.optionService = optionService;
		this.userService = userService;
		this.printJobManager = printJobManager;
		this.notificationService = notificationService;
	}

	@Override
	public PrintJob printDocuments(PrintJob printJob) {
		boolean success = true;
		for (PrintJobDocument printJobDocument : printJob.getDocuments()) {
			success &= this.printDocumentHelper(printJobDocument);
		}
		return printJobManager.notify(printJob, success ? PrintStatus.WAITING : PrintStatus.FAILED);
	}

	@Override
	public void setDefaultPrinter(String name) {
		if (!this.isRegisteredPrinter(name)) {
			throw new IllegalArgumentException(name + " is not a registered printer");
		}
		this.optionService.setOptionByName(Option.DEFAULT_PRINTER, name);
	}

	@Override
	public Set<PrintService> findRegisteredPrinters() {
		Set<PrintService> ret = new HashSet<>();
		Collections.addAll(ret, PrinterJob.lookupPrintServices());
		return ret;
	}

	@Override
	public boolean isRegisteredPrinter(String name) {
		for (PrintService printService : this.findRegisteredPrinters()) {
			if (printService.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public PrintService getPrinterForName(String name) {
		for (PrintService service : this.findRegisteredPrinters()) {
			if (service.getName().equalsIgnoreCase(name)) {
				return service;
			}
		}
		return null;
	}

	@Override
	public PrintService getDefaultPrinter() {
		return this.getPrinterForName(this.getDefaultPrinterName());
	}

	@Override
	public PrintJob doPrintJob(PrintJob job) {
		PrintStatus newStatus = PrintStatus.WAITING;
		boolean success = true;
		if(userService.currentUserAllowedToPrint()) {
			for (PrintJobDocument document : job.getDocuments()) {
				success &= this.printDocumentHelper(document);
			}
		} else {
			newStatus = PrintStatus.APPROVAL;
		}
		if (!success) {
			newStatus = PrintStatus.FAILED;
		}
		return this.printJobManager.notify(job, newStatus);
	}

	@Override
	public PrintJob doPrintJob(Long jobId) {
		return doPrintJob(printJobManager.findPrintJob(jobId));
	}

	@Override
	public List<PrintJob> doPrintJobs(Iterable<PrintJob> jobs) {
		List<PrintJob> printJobs = new ArrayList<>();
		for (PrintJob job : jobs) {
			printJobs.add(this.doPrintJob(job));
		}
		return printJobs;
	}

	private String getDefaultPrinterName() {
		return this.optionService.getOptionByName(Option.DEFAULT_PRINTER).getValue();
	}

	private boolean printDocumentHelper(PrintJobDocument printJobDocument) {
		Document document = printJobDocument.getDocument();
		try {
			PDDocument pdfDoc = PDDocument.load(document.getFileHandle());
			PrinterJob printerJob = PrinterJob.getPrinterJob();
			printerJob.setPrintService(getDefaultPrinter());
			printerJob.setJobName(document.getName());
			printerJob.setCopies(printJobDocument.getCount());
			pdfDoc.silentPrint(printerJob);
			pdfDoc.close();
		} catch (IOException e) {
			LOGGER.error(MessageFormat.format("Exception while loading a PDF - Maybe {0} is corrupt?", document.getFilePath()), e);
			notificationService.createNotification(
					UserAuthority.MANAGE_DOCUMENTS,
					"notification.broken_pdf",
					"/documents/edit?id=" + document.getId()
			);
			return false;
		} catch (PrinterException e) {
			LOGGER.error("Default printer seems not to support 2D printing, you should choose another one. An admin has been notified", e);
			notificationService.createNotification(UserAuthority.EDIT_OPTIONS,
					"notification.printer_not_supported",
					"/options/show"
			);
			return false;
		}
		return true;
	}
}

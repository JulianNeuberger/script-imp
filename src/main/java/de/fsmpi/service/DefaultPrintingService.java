package de.fsmpi.service;

import de.fsmpi.model.document.Document;
import de.fsmpi.model.option.Option;
import de.fsmpi.model.print.PrintJob;
import de.fsmpi.model.print.PrintStatus;
import de.fsmpi.model.user.User;
import de.fsmpi.repository.PrintJobRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.util.*;

@Service
public class DefaultPrintingService implements PrintingService {

    private final OptionService optionService;
    private final PrintJobRepository printJobRepository;
    private final PrintJobManager printJobManager;

    @Autowired
    public DefaultPrintingService(PrintJobRepository printJobRepository,
                                  OptionService optionService,
                                  PrintJobManager printJobManager) {
        this.printJobRepository = printJobRepository;
        this.optionService = optionService;
        this.printJobManager = printJobManager;
    }

    @Override
    public PrintJob printDocument(Document document) {
        return this.printDocuments(Collections.singleton(document));
    }

    @Override
    public PrintJob printDocuments(Collection<Document> documents) {
        PrintJob printJob = new PrintJob();
        printJob.setDocuments(documents);
        printJob.setCreatedDate(DateTime.now());

        boolean success = true;
        for (Document document : documents) {
            success &= this.printDocumentHelper(document);
        }
        printJob.setStatus(success ? PrintStatus.WAITING : PrintStatus.FAILED);

        printJob = this.printJobRepository.save(printJob);

        return printJob;
    }

    @Override
    public void setDefaultPrinter(String name) {
        if(!this.isRegisteredPrinter(name)) {
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
            if(printService.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public PrintService getPrinterForName(String name) {
        for (PrintService service : this.findRegisteredPrinters()) {
            if(service.getName().equalsIgnoreCase(name)) {
                return service;
            }
        }
        return null;
    }

    /**
     * prints if current user has privileges to do so, or posts a request for an admin to print it otherwise
     * @return the List of PrintJob objects created for this request
     */
    @Override
    public PrintJob tryPrint(Collection<Document> documents) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user.canPrint()) {
            return this.printDocuments(documents);
        } else {
            return this.printJobManager.addPrintJobToApprove(documents);
        }
    }

    /**
     * prints if current user has privileges to do so, or posts a request for an admin to print it otherwise
     * @return the PrintJob object created for this request
     */
    @Override
    public PrintJob tryPrint(Document document) {
        return this.tryPrint(Collections.singleton(document));
    }

    @Override
    public PrintService getDefaultPrinter() {
        return this.getPrinterForName(this.getDefaultPrinterName());
    }

    @Override
    public PrintJob doPrintJob(PrintJob job) {
        boolean success = true;
        job.setStatus(PrintStatus.WAITING);
        for (Document document : job.getDocuments()) {
            success &= this.printDocumentHelper(document);
        }
        if(!success) {
            job.setStatus(PrintStatus.FAILED);
        }
        return this.printJobRepository.save(job);
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

    private boolean printDocumentHelper(Document document) {
        try (PDDocument pdf = PDDocument.load(document.getFileHandle())) {
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            PDFPageable pdfPageable = new PDFPageable(pdf);
            printerJob.setPageable(pdfPageable);
            printerJob.setPrintService(this.getDefaultPrinter());
            printerJob.print(new HashPrintRequestAttributeSet());
//            this.printJobManager.addPrintJobListening(printJob, printerJob);
        } catch (PrinterException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

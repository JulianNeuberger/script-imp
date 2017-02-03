package de.fsmpi.service;

import de.fsmpi.misc.PrintJobUpdater;
import de.fsmpi.model.document.Document;
import de.fsmpi.model.print.PrintJob;
import de.fsmpi.model.print.PrintStatus;
import de.fsmpi.model.user.UserAuthority;
import de.fsmpi.repository.PrintJobRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.DocPrintJob;
import javax.servlet.ServletContext;
import java.util.Collection;

/**
 * Created by Julian on 26.01.2017.
 */
@Service
public class PrintJobManager {
    private final PrintJobRepository printJobRepository;
    private final NotificationService notificationService;
    private final ServletContext servletContext;

    @Autowired
    public PrintJobManager(PrintJobRepository printJobRepository,
                           NotificationService notificationService,
                           ServletContext servletContext) {
        this.printJobRepository = printJobRepository;
        this.notificationService = notificationService;
        this.servletContext = servletContext;
    }

    public PrintJob addPrintJobToApprove(Collection<Document> documents) {
        PrintJob printJob = new PrintJob();
        printJob.setDocuments(documents);
        printJob.setCreatedDate(DateTime.now());
        printJob.setStatus(PrintStatus.APPROVAL);
        notificationService.createNotification(
                UserAuthority.DO_PRINT,
                "notification.new_print",
                "/print/show/jobs?status=APPROVAL"
        );
        return this.printJobRepository.save(printJob);
    }

    public void addPrintJobListening(PrintJob dataBaseObject, DocPrintJob apiObject) {
        new PrintJobUpdater(dataBaseObject, apiObject);
    }

    public void printJobUpdated(PrintJob printJob) {
        this.printJobRepository.save(printJob);
    }
}

package de.fsmpi.service;

import de.fsmpi.misc.PrintJobUpdater;
import de.fsmpi.model.print.PrintJob;
import de.fsmpi.model.print.PrintJobDocument;
import de.fsmpi.model.print.PrintStatus;
import de.fsmpi.model.user.User;
import de.fsmpi.model.user.UserAuthority;
import de.fsmpi.repository.PrintJobRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.print.DocPrintJob;
import java.util.Collection;

@Service
public class PrintJobManager {
    private final PrintJobRepository printJobRepository;
    private final NotificationService notificationService;

    @Autowired
    public PrintJobManager(PrintJobRepository printJobRepository,
                           NotificationService notificationService) {
        this.notificationService = notificationService;
        this.printJobRepository = printJobRepository;
    }

	@SuppressWarnings("WeakerAccess")
    public PrintJob findPrintJob(Long id) {
    	return printJobRepository.findOne(id);
	}

	public Page<PrintJob> findAllOrderedByCreationDate(Pageable pageable) {
    	return printJobRepository.findAllByOrderByCreatedDateDesc(pageable);
	}

	public Page<PrintJob> findAllFilteredByStatusOrderedByCreationDate(Pageable pageable, PrintStatus status) {
		return printJobRepository.findByStatusOrderByCreatedDateDesc(pageable, status);
	}

    public PrintJob createPrintJobFromDocuments(User issuedBy,
												Collection<PrintJobDocument> printJobDocuments,
												PrintStatus status) {
		PrintJob printJob = new PrintJob();
		printJob.setDocuments(printJobDocuments);
		printJob.setCreatedDate(DateTime.now());
		printJob.setStatus(status);
		printJob.setIssuedBy(issuedBy);
		return printJobRepository.save(printJob);
    }

	@SuppressWarnings("WeakerAccess")
	public PrintJob notify(PrintJob job, PrintStatus printStatus) {
		PrintStatus prevStatus = job.getStatus();
		job.setStatus(printStatus);
		switch(printStatus) {
			case DONE:
				break;
			case WAITING:
				break;
			case FAILED:
				break;
			case CANCELED:
				if(prevStatus.equals(PrintStatus.APPROVAL)) {
					notificationService.createNotification(
							job.getIssuedBy(),
							"notification.your_job_canceled",
							"/"
					);
				}
				break;
			case APPROVAL:
				notificationService.createNotification(
						UserAuthority.DO_PRINT,
						"notification.new_print",
						"/print/show/jobs?status=APPROVAL"
				);
				break;
		}
		return this.printJobRepository.save(job);
	}

	public PrintJob notify(Long jobId, PrintStatus printStatus) {
		return notify(printJobRepository.findOne(jobId), printStatus);
    }

    public void addPrintJobListening(PrintJob dataBaseObject, DocPrintJob apiObject) {
        new PrintJobUpdater(dataBaseObject, apiObject);
    }

    public void printJobUpdated(PrintJob printJob) {
        this.printJobRepository.save(printJob);
    }
}

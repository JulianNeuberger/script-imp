package de.fsmpi.misc;

import de.fsmpi.model.print.PrintJob;
import de.fsmpi.model.print.PrintStatus;
import de.fsmpi.service.PrintJobManager;

import javax.print.DocPrintJob;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;

/**
 * Created by Julian on 26.01.2017.
 */
public class PrintJobUpdater implements PrintJobListener {

    private PrintJob job;
    private DocPrintJob apiJob;
    private PrintJobManager manager;

    public PrintJobUpdater(PrintJob job, DocPrintJob apiJob) {
        this.job = job;
        this.apiJob = apiJob;
    }

    @Override
    public void printDataTransferCompleted(PrintJobEvent pje) {
    }

    @Override
    public void printJobCompleted(PrintJobEvent pje) {

    }

    @Override
    public void printJobFailed(PrintJobEvent pje) {
        this.job.setStatus(PrintStatus.FAILED);


    }

    @Override
    public void printJobCanceled(PrintJobEvent pje) {
        this.job.setStatus(PrintStatus.CANCELED);
        this.destroyListener();
    }

    @Override
    public void printJobNoMoreEvents(PrintJobEvent pje) {

    }

    @Override
    public void printJobRequiresAttention(PrintJobEvent pje) {

    }

    private void destroyListener() {
        // unregister from listener, so garbage collection can happen
        this.apiJob.removePrintJobListener(this);
    }
}

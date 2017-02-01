package de.fsmpi.service;

import de.fsmpi.model.document.Document;
import de.fsmpi.model.print.PrintJob;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by Julian on 12.12.2016.
 */
@Service
public interface PricingService {

    BigDecimal getPriceForPages(int pages);

    BigDecimal getPriceForDocument(Document document);

    BigDecimal getPriceForDocuments(Iterable<Document> documents);

    BigDecimal getPriceForPrintJob(PrintJob printJob);

    BigDecimal getPriceForPrintJobs(Iterable<PrintJob> printJobs);
}

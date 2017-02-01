package de.fsmpi.service;

import de.fsmpi.model.document.Document;
import de.fsmpi.model.option.Option;
import de.fsmpi.model.print.PrintJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by Julian on 12.12.2016.
 */
@Service
public class PricingServiceImpl implements PricingService {

    private final OptionService optionService;

    @Autowired
    public PricingServiceImpl(OptionService optionService) {
        this.optionService = optionService;
    }

    @Override
    public BigDecimal getPriceForPages(int pages) {
        Integer costPerPage = Integer.valueOf(optionService.getOptionByName(Option.COST_PER_PAGE).getValue());
        return new BigDecimal(costPerPage).divide(new BigDecimal(100)).multiply(new BigDecimal(pages));
    }

    @Override
    public BigDecimal getPriceForDocument(Document document) {
        if(document.getPages() == null) {
            return BigDecimal.ZERO;
        }
        return this.getPriceForPages(document.getPages());
    }

    @Override
    public BigDecimal getPriceForDocuments(Iterable<Document> documents) {
        BigDecimal cost = BigDecimal.ZERO;
        for (Document document : documents) {
            cost = cost.add(this.getPriceForDocument(document));
        }
        return cost;
    }

    @Override
    public BigDecimal getPriceForPrintJob(PrintJob printJob) {
        return this.getPriceForDocuments(printJob.getDocuments());
    }

    @Override
    public BigDecimal getPriceForPrintJobs(Iterable<PrintJob> printJobs) {
        BigDecimal cost = BigDecimal.ZERO;
        for (PrintJob printJob : printJobs) {
            cost = cost.add(this.getPriceForPrintJob(printJob));
        }
        return cost;
    }
}
package de.fsmpi.service;

import de.fsmpi.model.document.Document;
import de.fsmpi.model.option.Option;
import de.fsmpi.model.print.PrintJob;
import de.fsmpi.model.print.PrintJobDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;

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
	public BigDecimal getPriceForPrintJob(PrintJob printJob) {
		Integer totalPages = 0;
		BigDecimal pricePerPage = new BigDecimal(optionService.getOptionByName(Option.COST_PER_PAGE).getValue());
		Collection<PrintJobDocument> documents = printJob.getDocuments();
		for (PrintJobDocument document : documents) {
			Integer pages = document.getDocument().getPages();
			if(pages == null) {
				pages = 0;
			}
			Integer count = document.getCount();
			if(count == null) {
				count = 0;
			}
			totalPages += pages * count;
		}
		return pricePerPage.multiply(new BigDecimal(totalPages));
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
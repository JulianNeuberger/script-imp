package de.fsmpi.service;

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
	public BigDecimal getPriceForPrintJob(PrintJob printJob) {
		Integer totalPages = printJob.getNumberOfPages();
		BigDecimal pricePerPage = new BigDecimal(optionService.getOptionByName(Option.COST_PER_PAGE).getValue());
		return pricePerPage.
				multiply(new BigDecimal(totalPages)).
				divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
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
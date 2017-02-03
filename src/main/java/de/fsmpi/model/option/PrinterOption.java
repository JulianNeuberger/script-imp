package de.fsmpi.model.option;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.print.PrintService;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Julian on 26.01.2017.
 */
@Entity
@DiscriminatorValue("printer")
public class PrinterOption<V> extends Option<PrintService> {

	static {
		Option.addSubclass(PrinterOption.class);
	}

	@Override
	public PrintService getValueTypeSafe() {
		PrintService[] printServices = PrinterJob.lookupPrintServices();
		for (PrintService printService : printServices) {
			if (printService.getName().equalsIgnoreCase(this.value)) {
				return printService;
			}
		}
		return null;
	}

	@Override
	public Iterable<String> getPossibleValues() {
		List<PrintService> printServices = Arrays.asList(PrinterJob.lookupPrintServices());
		List<String> printerNames = new ArrayList<>();
		for (PrintService printService : printServices) {
			printerNames.add(printService.getName());
		}
		return printerNames;
	}

	@Override
	public boolean hasPossibleValues() {
		return true;
	}
}

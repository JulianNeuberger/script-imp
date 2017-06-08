package de.fsmpi.controller;

import de.fsmpi.model.user.Cart;
import de.fsmpi.model.print.PrintJob;
import de.fsmpi.model.print.PrintStatus;
import de.fsmpi.model.user.User;
import de.fsmpi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Controller
@RequestMapping("/print")
public class PrintController extends BaseController {

//	private static final Logger LOGGER = LoggerFactory.getLogger(PrintController.class);
	private final PricingService pricingService;
	private final PrintJobManager printJobManager;
	private final PrintingService printingService;

	@Autowired
	public PrintController(PrintJobManager printJobManager,
						   NotificationService notificationService,
						   PrintingService printingService,
						   PricingService pricingService,
						   CartService cartService) {
		super(notificationService, cartService);
		this.printJobManager = printJobManager;
		this.printingService = printingService;
		this.pricingService = pricingService;
	}

	@RequestMapping("/cart")
	public String printCart(Model model) {
		Cart cart = getCartOfUserOrCreate();
		User user = getCurrentUserOrNull();
		PrintJob printJob =
				printJobManager.createPrintJobFromDocuments(user, cart.getDocuments(), PrintStatus.APPROVAL);
		printJob = printingService.doPrintJob(printJob);
		boolean printJobsNeedApproval = printJob.getStatus() == PrintStatus.APPROVAL;
		BigDecimal cost = pricingService.getPriceForPrintJob(printJob);
		cartService.clearCart(cart);
		model.addAttribute("cost", cost.doubleValue());
		model.addAttribute("approval", printJobsNeedApproval);
		return "/pages/user/print-success";
	}

	@RequestMapping("/job")
	public String printJob(@RequestParam("id") Long jobId,
						   Model model) {
		PrintJob job = this.printingService.doPrintJob(jobId);
		model.addAttribute("cost", pricingService.getPriceForPrintJob(job));
		model.addAttribute("approval", job.getStatus() == PrintStatus.APPROVAL);
		return "/pages/user/print-success";
	}

	@RequestMapping("/show/jobs")
	public String showPrintJobs(Model model,
								@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
								@RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
								@RequestParam(value = "status", required = false) PrintStatus status) {
		Page<PrintJob> printJobsPage;
		Pageable pageable = new PageRequest(page, size);
		if (status == null) {
			printJobsPage = this.printJobManager.findAllOrderedByCreationDate(pageable);
		} else {
			printJobsPage = this.printJobManager.findAllFilteredByStatusOrderedByCreationDate(pageable, status);
		}
		model.addAttribute("printJobsPage", printJobsPage);
		model.addAttribute("printStates", PrintStatus.values());
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("status", status);
		model.addAttribute("pricingService", pricingService);

		return "/pages/admin/show-print-jobs";
	}

	@RequestMapping(path = "/job/approve", params = "deny")
	public String deny(HttpServletRequest request, @RequestParam("id") Long printJobId) {
		printJobManager.notify(printJobId, PrintStatus.CANCELED);
		return "redirect:" + request.getHeader("referer");
	}

	@RequestMapping(path = "/job/approve", params = "approve")
	public String approve(HttpServletRequest request, @RequestParam("id") Long printJobId) {
		this.printingService.doPrintJob(printJobId);
		return "redirect:" + request.getHeader("referer");
	}
}

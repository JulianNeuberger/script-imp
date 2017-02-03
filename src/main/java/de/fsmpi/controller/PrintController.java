package de.fsmpi.controller;

import de.fsmpi.misc.Cart;
import de.fsmpi.model.document.Document;
import de.fsmpi.model.print.PrintJob;
import de.fsmpi.model.print.PrintStatus;
import de.fsmpi.repository.DocumentRepository;
import de.fsmpi.repository.PrintJobRepository;
import de.fsmpi.service.CartService;
import de.fsmpi.service.NotificationService;
import de.fsmpi.service.PricingService;
import de.fsmpi.service.PrintingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(PrintController.class);

	private final DocumentRepository documentRepository;
	private final PrintJobRepository printJobRepository;
	private final PricingService pricingService;
	private final PrintingService printingService;

	@Autowired
	public PrintController(DocumentRepository documentRepository,
						   PrintJobRepository printJobRepository,
						   NotificationService notificationService,
						   PrintingService printingService,
						   PricingService pricingService,
						   CartService cartService) {
		super(notificationService, cartService);
		this.documentRepository = documentRepository;
		this.printJobRepository = printJobRepository;
		this.printingService = printingService;
		this.pricingService = pricingService;
	}

	@RequestMapping("/document")
	public String print(@RequestParam("id") Long documentId, Model model) {
		Document doc = this.documentRepository.findOne(documentId);
		PrintJob job = this.printingService.tryPrint(doc);
		boolean printJobsNeedApproval = job.getStatus() == PrintStatus.APPROVAL;
		model.addAttribute("cost", pricingService.getPriceForPrintJob(job));
		model.addAttribute("approval", printJobsNeedApproval);
		return "/pages/user/print-success";
	}

	@RequestMapping("/cart")
	public String printCart(Model model) {
		Cart cart = getCartOfUserOrNull();
		if (cart != null) {
			PrintJob printJob = this.printingService.tryPrint(cart.getDocuments());
			boolean printJobsNeedApproval = printJob.getStatus() == PrintStatus.APPROVAL;
			BigDecimal cost = pricingService.getPriceForPrintJob(printJob);
			cartService.clearCart(cart);
			model.addAttribute("cost", cost.doubleValue());
			model.addAttribute("approval", printJobsNeedApproval);
		} else {
			LOGGER.info("A user could reach /print/cart without having a cart! This should not happen!");
			// FIXME: die a horrible death
		}
		return "/pages/user/print-success";
	}

	@RequestMapping("/job")
	public String printJob(@RequestParam("id") Long jobId,
						   Model model) {
		PrintJob job = this.printJobRepository.findOne(jobId);
		job = this.printingService.printDocuments(job.getDocuments());
		boolean printJobsNeedApproval = job.getStatus() == PrintStatus.APPROVAL;
		model.addAttribute("cost", pricingService.getPriceForPrintJob(job));
		model.addAttribute("approval", printJobsNeedApproval);
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
			printJobsPage = this.printJobRepository.findAllByOrderByCreatedDateDesc(pageable);
		} else {
			printJobsPage = this.printJobRepository.findByStatusOrderByCreatedDateDesc(pageable, status);
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
		PrintJob job = this.printJobRepository.findOne(printJobId);
		job.setStatus(PrintStatus.CANCELED);
		this.printJobRepository.save(job);
		return "redirect:" + request.getHeader("referer");
	}

	@RequestMapping(path = "/job/approve", params = "approve")
	public String approve(HttpServletRequest request, @RequestParam("id") Long printJobId) {
		PrintJob job = this.printJobRepository.findOne(printJobId);
		this.printingService.doPrintJob(job);
		return "redirect:" + request.getHeader("referer");
	}
}

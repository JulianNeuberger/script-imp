package de.fsmpi.controller;

import de.fsmpi.service.CartService;
import de.fsmpi.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController extends BaseController implements org.springframework.boot.autoconfigure.web.ErrorController {

	private static final String ERROR_PATH = "/error";

	@Autowired
	public ErrorController(NotificationService notificationService,
						   CartService cartService) {
		super(notificationService, cartService);
	}

	@RequestMapping(ERROR_PATH)
	public String error(Model model) {
		return "redirect:/";
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}
}

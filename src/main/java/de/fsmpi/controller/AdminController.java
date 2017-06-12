package de.fsmpi.controller;

import de.fsmpi.service.CartService;
import de.fsmpi.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
	@Autowired
	public AdminController(CartService cartService,
						   NotificationService notificationService) {
		super(notificationService, cartService);
	}

	@RequestMapping("/overview")
	public String overview() {
		return "pages/admin/show-overview";
	}


}

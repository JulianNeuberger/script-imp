package de.fsmpi.controller;

import de.fsmpi.service.CartService;
import de.fsmpi.service.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController extends BaseController {

    public HomeController(NotificationService notificationService,
                          CartService cartService) {
        super(notificationService, cartService);
    }

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("name", "Test");
        return "/pages/user/home";
    }
}

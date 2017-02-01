package de.fsmpi.controller;

import de.fsmpi.service.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
//@RequestMapping("/user")
public class UserController extends BaseController {

    public UserController(NotificationService notificationService) {
        super(notificationService);
    }

    @RequestMapping(path = "/user/login", method = RequestMethod.GET)
    public String login() {
        return "/pages/user/login";
    }
}

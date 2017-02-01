package de.fsmpi.controller;

import de.fsmpi.model.user.User;
import de.fsmpi.repository.UserRepository;
import de.fsmpi.service.NotificationService;
import de.fsmpi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
//@RequestMapping("/user")
public class UserController extends BaseController {
    private final UserService userService;

    @Autowired
    public UserController(NotificationService notificationService,
                          UserService userService) {
        super(notificationService);
        this.userService = userService;
    }

    @RequestMapping(path = "/user/login", method = RequestMethod.GET)
    public String login() {
        return "/pages/user/login";
    }

    @RequestMapping(path = "/user/register", method = RequestMethod.GET)
    public String register() {
        return "/pages/user/register";
    }

    @RequestMapping(path = "/user/register", method = RequestMethod.POST)
    public String register(@RequestParam String username,
                           @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        this.userService.register(user);
        return "redirect:/user/login";
    }
}

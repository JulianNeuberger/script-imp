package de.fsmpi.controller;

import de.fsmpi.model.user.User;
import de.fsmpi.service.CartService;
import de.fsmpi.service.NotificationService;
import de.fsmpi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController extends BaseController {
    private final UserService userService;

    @Autowired
    public UserController(NotificationService notificationService,
                          CartService cartService,
                          UserService userService) {
        super(notificationService, cartService);
        this.userService = userService;
    }

    @RequestMapping(path = "/user/login", method = RequestMethod.GET)
    public String login() {
        return "pages/user/login";
    }

    @RequestMapping(path = "/user/show/self")
    public String showSelf(Model model) {
    	User user = getCurrentUserOrNull();
    	if(user != null) {
    		model.addAttribute("user", user);
		} else {
			// FIXME: die...
		}
		return "pages/user/show-user";
	}

	@RequestMapping(path = "/user/save/self", method = RequestMethod.POST)
	public String saveSelf(Model model,
						   @RequestParam(required = false, defaultValue = "") String firstName,
						   @RequestParam(required = false, defaultValue = "") String lastName,
						   @RequestParam(required = false, defaultValue = "") String mail,
						   @RequestParam(required = false, defaultValue = "") String password,
						   @RequestParam(required = false, defaultValue = "") String repeatPassword) {
		User user = getCurrentUserOrNull();
		if(user != null) {
			firstName = firstName.trim();
			lastName = lastName.trim();
			mail = mail.trim();
			password = password.trim();
			user = userService.updateProfile(user, password, firstName, lastName, mail);
			model.addAttribute("user", user);
		} else {
			// FIXME: die horribly
		}
		return "pages/user/show-user";
	}

	@RequestMapping(path = "/user/register", method = RequestMethod.GET)
	public String register() {
        return "pages/user/register";
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

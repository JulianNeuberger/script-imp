package de.fsmpi.controller;

import de.fsmpi.model.user.User;
import de.fsmpi.model.user.UserAuthority;
import de.fsmpi.service.CartService;
import de.fsmpi.service.NotificationService;
import de.fsmpi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

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

	@RequestMapping(path = "/user/edit/self", method = RequestMethod.GET)
	public String showSelf(Model model) {
		User user = getCurrentUserOrNull();
		if (user != null) {
			model.addAttribute("user", user);
		} else {
			return "redirect:/user/login/";
		}
		return "pages/user/show-user";
	}

	@RequestMapping(path = "/user/edit/self", method = RequestMethod.POST)
	public String saveSelf(Model model,
						   @RequestParam(required = false, defaultValue = "") String firstName,
						   @RequestParam(required = false, defaultValue = "") String lastName,
						   @RequestParam(required = false, defaultValue = "") String mail,
						   @RequestParam(required = false, defaultValue = "") String password,
						   @RequestParam(required = false, defaultValue = "") String repeatPassword) {
		User user = getCurrentUserOrNull();
		if (user != null) {
			firstName = firstName.trim();
			lastName = lastName.trim();
			mail = mail.trim();
			password = password.trim();
			user = userService.updateProfile(user, password, firstName, lastName, mail);
			model.addAttribute("user", user);
		} else {
			return "redirect:/user/login/";
		}
		return "pages/user/show-user";
	}

	@RequestMapping(path = "/user/edit", method = RequestMethod.GET)
	public String showOne(Model model, HttpServletResponse response, @RequestParam String username) {
		User userToEdit = userService.getById(username);
		model.addAttribute("userToEdit", userToEdit);
		model.addAttribute("authorities", UserAuthority.values());
		return "pages/admin/edit-single-user";
	}

	@RequestMapping(path = "/user/edit", method = RequestMethod.POST)
	public String saveOne(@RequestParam String username,
						  @RequestParam Set<UserAuthority> authorities) {
		userService.updateAuthorities(username, authorities);
		return "redirect:/user/show/all";
	}

	@RequestMapping(path = "/user/delete", method = RequestMethod.POST)
	public String delete(@RequestParam String username) {
		userService.deleteOne(username);
		return "redirect:/user/show/all";
	}

	@RequestMapping(path = "/user/show/all", method = RequestMethod.GET)
	public String showAll(Model model) {
		Iterable<User> users = userService.getAll();
		model.addAttribute("users", users);
		return "pages/admin/show-users";
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

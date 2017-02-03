package de.fsmpi.controller;

import de.fsmpi.misc.Cart;
import de.fsmpi.model.user.User;
import de.fsmpi.service.CartService;
import de.fsmpi.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

public class BaseController {
    protected final NotificationService notificationService;
	protected final CartService cartService;

    @Autowired
    public BaseController(NotificationService notificationService,
						  CartService cartService) {
        this.notificationService = notificationService;
        this.cartService = cartService;
    }

    @ModelAttribute
    public void user(Model model) {
        model.addAttribute("user", getCurrentUserOrNull());
    }

    @ModelAttribute
    public void notifications(Model model) {
        User user = getCurrentUserOrNull();
        if(user != null) {
            model.addAttribute("hasNewNotifications", notificationService.hasNewNotificationsForUser(user));
            model.addAttribute("newNotifications", notificationService.getNewNotificationsForUser(user));
        } else {
            model.addAttribute("hasNewNotifications", false);
        }
    }

    @ModelAttribute
    public void cartItemCount(Model model) {
        Cart cart = getCartOfUserOrCreate();
        if(cart != null) {
			model.addAttribute("cartItemCount", cart.getItemCount());
		} else {
    		model.addAttribute("cartItemCount", 0);
		}
	}

    User getCurrentUserOrNull() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof User) {
            return (User) principal;
        } else {
            return null;
        }
    }

	/**
	 * Will generate a cart and assign it, if user has none
	 *
	 * @return null if no user is logged in, the (generated) cart otherwise
	 */
	Cart getCartOfUserOrCreate() {
    	User user = getCurrentUserOrNull();
    	if(user == null) {
    		return null;
		}
		Cart cart = user.getCart();
		if(cart == null) {
			user = cartService.assignNewCartToUser(user);
		}
		return user.getCart();
	}
}

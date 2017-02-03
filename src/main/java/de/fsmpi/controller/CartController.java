package de.fsmpi.controller;

import de.fsmpi.misc.Cart;
import de.fsmpi.repository.DocumentRepository;
import de.fsmpi.service.CartService;
import de.fsmpi.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/cart")
public class CartController extends BaseController {
    private final DocumentRepository documentRepository;

    @Autowired
    public CartController(DocumentRepository documentRepository,
						  CartService cartService,
                          NotificationService notificationService) {
        super(notificationService, cartService);
        this.documentRepository = documentRepository;
    }

    @RequestMapping("/add/document")
    public String add(HttpServletRequest request,
                      @RequestParam("id") Long documentId,
                      @RequestParam(value = "backLink", required = false) String backLink) {
		Cart cart = getCartOfUserOrNull();
		if(cart != null) {
			cartService.addItemToCart(cart, this.documentRepository.findOne(documentId));
		}
        String referrer;
        if(backLink == null || backLink.trim().length() == 0) {
            referrer = request.getHeader("referer");
        } else {
            referrer = backLink;
        }

        return "redirect:" + referrer;
    }

    @RequestMapping("/remove/document")
    public String remove(HttpServletRequest request,
						 @RequestParam("id") Long documentId) {
		Cart cart = getCartOfUserOrNull();
		cartService.removeItemFromCart(cart, this.documentRepository.findOne(documentId));
        String referrer = request.getHeader("referer");

        return "redirect:" + referrer;
    }

    @RequestMapping("/clear")
    public String clear() {
        Cart cart = getCartOfUserOrNull();
        if(cart != null) {
        	cart = this.cartService.clearCart(cart);
		}
        return "/"; // FIXME: return to previous site
    }

    @RequestMapping("/show")
    public String show(Model model) {
    	Cart cart = getCartOfUserOrNull();
        model.addAttribute("documents", cart.getDocuments());
        return "/pages/user/show-cart";
    }
}

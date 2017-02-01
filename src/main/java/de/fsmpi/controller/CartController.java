package de.fsmpi.controller;

import de.fsmpi.misc.Cart;
import de.fsmpi.repository.DocumentRepository;
import de.fsmpi.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Julian on 27.01.2017.
 */
@Controller
@RequestMapping("/cart")
@Scope("request")
public class CartController extends BaseController {
    private final DocumentRepository documentRepository;
    private final Cart cart;

    @Autowired
    public CartController(DocumentRepository documentRepository,
                          NotificationService notificationService,
                          Cart cart) {
        super(notificationService);
        this.documentRepository = documentRepository;
        this.cart = cart;
    }

    @RequestMapping("/add/document")
    public String add(Model model,
                      HttpServletRequest request,
                      @RequestParam("id") Long documentId,
                      @RequestParam(value = "backLink", required = false) String backLink) {
        cart.addDocumentToCart(this.documentRepository.findOne(documentId));
        String referrer;
        if(backLink == null || backLink.trim().length() == 0) {
            referrer = request.getHeader("referer");
        } else {
            referrer = backLink;
        }

        return "redirect:" + referrer;
    }

    @RequestMapping("/remove/document")
    public String remove(Model model, HttpServletRequest request, @RequestParam("id") Long documentId) {
        cart.removeDocumentFromCart(this.documentRepository.findOne(documentId));
        String referrer = request.getHeader("referer");

        return "redirect:" + referrer;
    }

    @RequestMapping("/clear")
    public String clear() {
        cart.clear();

        return "/";
    }

    @RequestMapping("/show")
    public String show(Model model) {
        model.addAttribute("documents", cart.getDocuments());
        return "/pages/user/show-cart";
    }
}

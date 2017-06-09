package de.fsmpi.controller;

import de.fsmpi.model.user.Cart;
import de.fsmpi.model.document.Document;
import de.fsmpi.model.print.PrintJobDocument;
import de.fsmpi.repository.DocumentRepository;
import de.fsmpi.service.CartService;
import de.fsmpi.service.NotificationService;
import de.fsmpi.service.PrintJobDocumentService;
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
    private final PrintJobDocumentService printJobDocumentService;

    @Autowired
    public CartController(DocumentRepository documentRepository,
						  CartService cartService,
						  PrintJobDocumentService printJobDocumentService,
                          NotificationService notificationService) {
        super(notificationService, cartService);
        this.documentRepository = documentRepository;
        this.printJobDocumentService = printJobDocumentService;
    }

    @RequestMapping("/add/document")
    public String add(HttpServletRequest request,
                      @RequestParam("id") Long documentId,
                      @RequestParam(value = "backLink", required = false) String backLink) {
		Cart cart = getCartOfUserOrCreate();
		Document document = documentRepository.findOne(documentId);
		cartService.addItemToCart(cart, printJobDocumentService.createPrintJobDocument(document));
        String referrer;
        if(backLink == null || backLink.trim().length() == 0) {
            referrer = request.getHeader("referer");
        } else {
            referrer = backLink;
        }

        return "redirect:" + referrer;
    }

    @RequestMapping("/remove/item")
    public String remove(HttpServletRequest request,
						 @RequestParam("id") Long printJobDocumentId) {
		Cart cart = getCartOfUserOrCreate();
		cartService.removeItemFromCart(cart, this.printJobDocumentService.findOne(printJobDocumentId));
        String referrer = request.getHeader("referer");

        return "redirect:" + referrer;
    }

    @RequestMapping("/clear")
    public String clear() {
        Cart cart = getCartOfUserOrCreate();
        if(cart != null) {
        	cart = this.cartService.clearCart(cart);
		}
        return "/"; // FIXME: return to previous site
    }

    @RequestMapping("/show")
    public String show(Model model) {
    	Cart cart = getCartOfUserOrCreate();
        model.addAttribute("documents", cart.getDocuments());
        return "pages/user/show-cart";
    }

    @RequestMapping("/item/copies")
	public String changeCopyCount(HttpServletRequest request,
								  @RequestParam Long id,
								  @RequestParam Integer copyCount) {
    	Cart cart = getCartOfUserOrCreate();
    	PrintJobDocument printJobDocument = cart.getItemForId(id);
		printJobDocumentService.updateCopyCount(printJobDocument, copyCount);
		return "redirect:/cart/show";
	}
}

package de.fsmpi.service;

import de.fsmpi.model.user.Cart;
import de.fsmpi.model.document.Document;
import de.fsmpi.model.print.PrintJobDocument;
import de.fsmpi.model.user.User;

public interface CartService {

	Cart addItemToCart(Cart cart, PrintJobDocument document);

	Cart addItemsToCart(Cart cart, Iterable<PrintJobDocument> documents);

	Cart removeItemFromCart(Cart cart, Document documen);

	Cart removeItemFromCart(Cart cart, PrintJobDocument document);

	Cart removeItemsFromCart(Cart cart, Iterable<PrintJobDocument> documents);

	User assignNewCartToUser(User user);

	Cart clearCart(Cart cart);
}

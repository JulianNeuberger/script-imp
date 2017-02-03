package de.fsmpi.service;

import de.fsmpi.misc.Cart;
import de.fsmpi.model.document.Document;
import de.fsmpi.model.user.User;

public interface CartService {

	Cart addItemToCart(Cart cart, Document document);

	Cart addItemsToCart(Cart cart, Iterable<Document> documents);

	Cart removeItemFromCart(Cart cart, Document document);

	Cart removeItemsFromCart(Cart cart, Iterable<Document> documents);

	User assignNewCartToUser(User user);

	Cart clearCart(Cart cart);
}

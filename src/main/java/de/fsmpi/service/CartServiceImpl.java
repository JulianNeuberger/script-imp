package de.fsmpi.service;

import de.fsmpi.misc.Cart;
import de.fsmpi.model.document.Document;
import de.fsmpi.model.user.User;
import de.fsmpi.repository.CartRepository;
import de.fsmpi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final UserRepository userRepository;

	@Autowired
	public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository) {
		this.cartRepository = cartRepository;
		this.userRepository = userRepository;
	}

	@Override
	public Cart addItemToCart(Cart cart, Document document) {
		cart.addDocumentToCart(document);
		return this.cartRepository.save(cart);
	}

	@Override
	public Cart addItemsToCart(Cart cart, Iterable<Document> documents) {
		for (Document document : documents) {
			cart = this.addItemToCart(cart, document);
		}
		return cart;
	}

	@Override
	public Cart removeItemFromCart(Cart cart, Document document) {
		cart.removeDocumentFromCart(document);
		return this.cartRepository.save(cart);
	}

	@Override
	public Cart removeItemsFromCart(Cart cart, Iterable<Document> documents) {
		for (Document document : documents) {
			cart = this.removeItemFromCart(cart, document);
		}
		return cart;
	}

	@Override
	public User assignNewCartToUser(User user) {
		Cart cart = new Cart();
		cart = this.cartRepository.save(cart);
		user.setCart(cart);
		return this.userRepository.save(user);
	}

	@Override
	public Cart clearCart(Cart cart) {
		cart.clear();
		return this.cartRepository.save(cart);
	}
}

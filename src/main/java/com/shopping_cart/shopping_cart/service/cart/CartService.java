package com.shopping_cart.shopping_cart.service.cart;

import com.shopping_cart.shopping_cart.exceptions.ResourceNotfoundException;
import com.shopping_cart.shopping_cart.model.Cart;
import com.shopping_cart.shopping_cart.repository.CartItemRepository;
import com.shopping_cart.shopping_cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cardIdGenerator = new AtomicLong(0);


    @Override
    public Cart getCart(Long id) {
        System.out.println("getCart called");
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new ResourceNotfoundException("Cart not found"));
        System.out.println("cart before totalAmount= " + cart);
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        System.out.println("cart after totalAmount= " + cart);
        return cartRepository.save(cart); // saving the cart with total amount and return it.
    }

    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Long generateCartId() {
        Cart cart = new Cart();
        Long newCardId = cardIdGenerator.incrementAndGet();
        cart.setId(newCardId);
        cartRepository.save(cart);
        return newCardId;
    }

}

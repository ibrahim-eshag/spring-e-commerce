package com.shopping_cart.shopping_cart.service.cart;

import com.shopping_cart.shopping_cart.model.Cart;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);

    void clearCart(Long id);

    BigDecimal getTotalPrice(Long id);

    Long generateCartId();
}

package com.shopping_cart.shopping_cart.service.cart;

import com.shopping_cart.shopping_cart.model.CartItem;

public interface ICartItemService {
    CartItem addItemToCart(Long cartId, Long productId, int quantity);

    void deleteItemFromCart(Long cartId, Long productId);

    CartItem updateItemInCart(Long cartId, Long productId, int quantity);

    CartItem getItemFromCart(Long cartId, Long productId);
}

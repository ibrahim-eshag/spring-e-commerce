package com.shopping_cart.shopping_cart.service.cart;

import com.shopping_cart.shopping_cart.exceptions.ResourceNotfoundException;
import com.shopping_cart.shopping_cart.model.Cart;
import com.shopping_cart.shopping_cart.model.CartItem;
import com.shopping_cart.shopping_cart.model.Product;
import com.shopping_cart.shopping_cart.repository.CartItemRepository;
import com.shopping_cart.shopping_cart.repository.CartRepository;
import com.shopping_cart.shopping_cart.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ICartService cartService;
    private final IProductService productService;

    @Override
    public CartItem addItemToCart(Long cartId, Long productId, int quantity) {
        // 1- get the cart
        Cart cart = cartService.getCart(cartId);

        // 2- get the product
        Product product = productService.getProductById(productId);

        // 3- check if  the product (item is already in the cart)
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());

        // 4- if yes then increase the quantity with the requested quantity.
        if (cartItem.getProduct().getId().equals(productId)) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            // 5- if no, then initiate a new cartItem entry.
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        }

        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
        return null;
    }

    @Override
    public void deleteItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem cartItem = getItemFromCart(cartId, productId);
        if (cartItem != null) {
            cart.removeItem(cartItem);
            cartRepository.save(cart);
            cartItemRepository.delete(cartItem); // NOTE: I don't know if i needed it
        }

    }

    @Override
    public CartItem updateItemInCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        CartItem cartItem = getItemFromCart(cartId, productId);

        if (cartItem != null) {
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(cartItem.getProduct().getPrice());
            cartItem.setTotalPrice();

            BigDecimal totalAmount = cart.getTotalAmount();
            cart.setTotalAmount(totalAmount);
            cartRepository.save(cart);
            return cartItemRepository.save(cartItem);
        }
        return null;
    }

    @Override
    public CartItem getItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotfoundException("item not found in cart"));
    }

}

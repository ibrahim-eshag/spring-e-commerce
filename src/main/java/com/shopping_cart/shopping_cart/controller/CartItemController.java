package com.shopping_cart.shopping_cart.controller;

import com.shopping_cart.shopping_cart.exceptions.ResourceNotfoundException;
import com.shopping_cart.shopping_cart.model.CartItem;
import com.shopping_cart.shopping_cart.response.ApiResponse;
import com.shopping_cart.shopping_cart.service.cart.ICartItemService;
import com.shopping_cart.shopping_cart.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cart-item")
public class CartItemController {
    private final ICartItemService cartItemService;
    private final ICartService cartService;

    @PostMapping("/add-to-cart")
    public ResponseEntity<ApiResponse> addCartItemToCart(
            @RequestParam(required = false) Long cartId,
            @RequestParam Long productId,
            @RequestParam Integer quantity
    ) {
        try {
            System.out.println("cartId +" + cartId + ", productId = " + productId + ", quantity = " + quantity);
            // NOTE: in the future we're going to add the functionality to generate
            // new cartId functionality in the user context
            if (cartId == null) {
                cartId = cartService.generateCartId();
            }

            CartItem cartItem = cartItemService.addItemToCart(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("add item to cart success", cartItem));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/remove-from-cart/{cartId}/{itemId}")
    public ResponseEntity<ApiResponse> removeCartItemFromCart(
            @PathVariable Long itemId,
            @PathVariable Long cartId
    ) {
        try {
            cartItemService.deleteItemFromCart(cartId, itemId);
            return ResponseEntity.ok(new ApiResponse("remove item from cart success", null));

        } catch (ResourceNotfoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update-quantity/{cartId}/{itemId}")
    public ResponseEntity<ApiResponse> updateCartItemQuantity(
            @PathVariable Long cartId,
            @PathVariable Long itemId,
            @RequestParam Integer quantity
    ) {
        try {
            cartItemService.updateItemInCart(cartId, itemId, quantity);
            return ResponseEntity.ok(new ApiResponse("update item quantity success", null));
        } catch (ResourceNotfoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }
}

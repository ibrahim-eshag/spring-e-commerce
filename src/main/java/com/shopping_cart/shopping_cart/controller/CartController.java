package com.shopping_cart.shopping_cart.controller;

import com.shopping_cart.shopping_cart.model.Cart;
import com.shopping_cart.shopping_cart.response.ApiResponse;
import com.shopping_cart.shopping_cart.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/${api.prefix}/cart")
@RequiredArgsConstructor
public class CartController {
    private final ICartService cartService;

    @GetMapping("/{cardId}")
    public ResponseEntity<ApiResponse> getCart(
            @PathVariable Long cardId
    ) {
        try {
            Cart cart = cartService.getCart(cardId);
            return ResponseEntity.ok(new ApiResponse("get cart success", cart));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse> clearCart(
            @PathVariable Long cartId
    ) {
        try {
            cartService.clearCart(cartId);
            return ResponseEntity.ok(new ApiResponse("clear cart success", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{cartId}/total-price")
    public ResponseEntity<ApiResponse> getTotalPrice(
            @PathVariable Long cartId
    ) {
        try {
            BigDecimal totalPrice = cartService.getTotalPrice(cartId);
            return ResponseEntity.ok(new ApiResponse("get total price success", totalPrice));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}

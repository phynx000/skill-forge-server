package com.skillforge.skillforge_api.controller;

import com.skillforge.skillforge_api.dto.request.UpdateQuantityCartRequest;
import com.skillforge.skillforge_api.dto.response.CartDTO;
import com.skillforge.skillforge_api.service.CartService;
import com.skillforge.skillforge_api.utils.annotation.ApiMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @GetMapping("/carts")
    @ApiMessage(value = "Get current user's cart")
    public ResponseEntity<CartDTO> getCart() {
        CartDTO cart = cartService.getCart();
        return ResponseEntity.ok(cart);
    }

    @PatchMapping("/carts/{itemId}")
    @ApiMessage(value = "Update entity in cart")
    public ResponseEntity<CartDTO.CardItemDTO> updateCart(@PathVariable Long itemId,
    @RequestBody UpdateQuantityCartRequest updateQuantityCartRequest) {
        CartDTO.CardItemDTO updatedItem = cartService.updateCart(itemId, updateQuantityCartRequest.getQuantity());
        return ResponseEntity.ok(updatedItem);
    }
}

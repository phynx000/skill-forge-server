package com.skillforge.skillforge_api.service;


import com.skillforge.skillforge_api.dto.mapper.CartMapper;
import com.skillforge.skillforge_api.dto.response.CartDTO;
import com.skillforge.skillforge_api.entity.Cart;
import com.skillforge.skillforge_api.entity.CartItem;
import com.skillforge.skillforge_api.entity.User;
import com.skillforge.skillforge_api.repository.CartItemRepository;
import com.skillforge.skillforge_api.repository.CartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CartService {

    private final CartRepository cardRepository;
    private final CartMapper cartMapper;
    private final UserService userService;
    private final CartItemRepository cartItemRepository;


    public CartService(CartRepository cardRepository, CartMapper cartMapper, UserService userService, CartItemRepository cartItemRepository) {
        this.cardRepository = cardRepository;
        this.cartMapper = cartMapper;
        this.userService = userService;
        this.cartItemRepository = cartItemRepository;
    }

    public CartDTO.CardItemDTO updateCart(Long itemId ,  int quantity){
        User user = userService.getCurrentUser();
        Optional<Cart> cart = cardRepository.findByUserId(user.getId());
        CartDTO.CardItemDTO cartItemDTO = new CartDTO.CardItemDTO();
        if (cart.isPresent()){
            Cart existingCart = cart.get();
            cartItemDTO = cartMapper.toCardItemDTO(existingCart.getItems().stream()
                    .filter(item -> item.getId().equals(itemId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Item not found in cart"))
                    ); // Assuming you want the first item

            cartItemDTO.setQuantity(quantity);
            CartItem cartItem = cartMapper.toCartItem(cartItemDTO);
            cartItem.setCourse(existingCart.getItems().stream()
                    .filter(item -> item.getId().equals(itemId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Item not found in cart"))
                    .getCourse()); // Set the course from the existing item
            cartItem.setCart(existingCart); // Set the cart for the item

            cartItemRepository.save(cartItem); // Save the updated cart item

            // cần update lại tổng giá trị của giỏ hàng
//            cardRepository.save(existingCart);
        } else {
           Cart newCart = new Cart();
           newCart.setUser(userService.getCurrentUser());
           cardRepository.save(newCart);
           updateCart(itemId, quantity);
        }
        return cartItemDTO;
    }
}

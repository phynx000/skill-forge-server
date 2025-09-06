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

import java.math.BigDecimal;
import java.util.ArrayList;
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
            // Chỉ filter 1 lần, lưu kết quả
            CartItem existingItem = existingCart.getItems().stream()
                    .filter(item -> item.getId().equals(itemId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Item not found in cart"));

// Sử dụng existingItem cho tất cả logic tiếp theo
            cartItemDTO = cartMapper.toCardItemDTO(existingItem);
            cartItemDTO.setQuantity(quantity);

// Cập nhật trực tiếp existingItem thay vì tạo mới
            existingItem.setQuantity(quantity);
            cartItemRepository.save(existingItem);
        } else {
            // Tạo cart mới và xử lý logic ngay tại đây, không gọi đệ quy
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setTotalItems(0);
            newCart.setTotalPrice(BigDecimal.ZERO);
            newCart.setItems(new ArrayList<>());
            Cart savedCart = cardRepository.save(newCart);

            // Tìm CartItem theo ID để update
            CartItem existingItem = cartItemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Item không tồn tại"));

            existingItem.setQuantity(quantity);
            existingItem.setCart(savedCart);
            cartItemRepository.save(existingItem);

            cartItemDTO = cartMapper.toCardItemDTO(existingItem);
            cartItemDTO.setQuantity(quantity);
        }
        return cartItemDTO;
    }


    public CartDTO getCart() {
        User user = userService.getCurrentUser();
        Optional<Cart> cart = cardRepository.findByUserId(user.getId());
        if (cart.isPresent()) {
            return cartMapper.toCartDTO(cart.get());
        } else {
            CartDTO emptyCart = new CartDTO();
            emptyCart.setId(null);
            emptyCart.setTotalItems(0);
            emptyCart.setTotalPrice(BigDecimal.ZERO);
            emptyCart.setItems(new ArrayList<>());
            return emptyCart;

        }
    }
}

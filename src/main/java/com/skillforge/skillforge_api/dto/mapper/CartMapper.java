package com.skillforge.skillforge_api.dto.mapper;


import com.skillforge.skillforge_api.dto.response.CartDTO;
import com.skillforge.skillforge_api.entity.Cart;
import com.skillforge.skillforge_api.entity.CartItem;
import com.skillforge.skillforge_api.service.UserService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {


    public CartDTO.CardItemDTO toCardItemDTO(CartItem cartItem) {
        CartDTO.CardItemDTO cardItemDTO = new CartDTO.CardItemDTO();
        cardItemDTO.setId(cartItem.getId());
        cardItemDTO.setCourseName(cartItem.getCourse().getTitle());
        cardItemDTO.setCourseId(cartItem.getCourse().getId());
        cardItemDTO.setImageUrl(cartItem.getCourse().getThumbnailUrl());
        cardItemDTO.setPrice(cartItem.getCourse().getPrice());
        cardItemDTO.setQuantity(cartItem.getQuantity()); // ← THIẾU DÒNG NÀY!
        return cardItemDTO;
    }

    public CartItem toCartItem(CartDTO.CardItemDTO cartItemDTO) {
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemDTO.getId());
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setPrice(cartItemDTO.getPrice());
        return cartItem;
    }

    public CartDTO toCartDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setTotalItems(cart.getTotalItems());
        cartDTO.setTotalPrice(cart.getTotalPrice());

        List<CartDTO.CardItemDTO> itemDTOs = cart.getItems().stream()
                .map(this::toCardItemDTO)
                .collect(Collectors.toList());
        cartDTO.setItems(itemDTOs);

        return cartDTO;
    }
}

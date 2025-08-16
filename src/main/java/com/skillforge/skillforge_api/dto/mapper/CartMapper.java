package com.skillforge.skillforge_api.dto.mapper;


import com.skillforge.skillforge_api.dto.response.CartDTO;
import com.skillforge.skillforge_api.entity.Cart;
import com.skillforge.skillforge_api.entity.CartItem;
import com.skillforge.skillforge_api.service.UserService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CartMapper {


    public CartDTO.CardItemDTO toCardItemDTO(CartItem cartItem) {
        CartDTO.CardItemDTO cardItemDTO = new CartDTO.CardItemDTO();
        cardItemDTO.setId(cartItem.getId());
        cardItemDTO.setCourseName(cartItem.getCourse().getTitle());
        cardItemDTO.setCourseId(cartItem.getCourse().getId());
        cardItemDTO.setImageUrl(cartItem.getCourse().getThumbnailUrl());
        cardItemDTO.setPrice(cartItem.getCourse().getPrice());
        return cardItemDTO;
    }

    public CartItem toCartItem(CartDTO.CardItemDTO cartItemDTO) {
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemDTO.getId());
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setPrice(cartItemDTO.getPrice());
        return cartItem;
    }
}

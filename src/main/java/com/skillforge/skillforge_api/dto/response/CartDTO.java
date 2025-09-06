package com.skillforge.skillforge_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CartDTO {
    private Long id;
    private int totalItems;
    private BigDecimal totalPrice;
    private List<CardItemDTO> items;

    public CartDTO() {

    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class CardItemDTO
    {
        private Long id;
        private String courseName;
        private Long courseId;
        private String imageUrl;
        private BigDecimal price;
        private int quantity;

        public CardItemDTO() {
            // Default constructor
        }
    }



}

package com.skillforge.skillforge_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int totalItems;

    private BigDecimal totalPrice;

    // quan hệ 1 nhiều với OrderItem => mỗi thẻ có thể chứa nhiều mặt hàng
    @OneToMany(mappedBy = "cart",fetch = FetchType.LAZY,orphanRemoval = true)
    private List<CartItem> items;


    public Cart() {

    }
}

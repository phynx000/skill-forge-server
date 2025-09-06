package com.skillforge.skillforge_api.repository;

import com.skillforge.skillforge_api.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // Additional query methods can be defined here if needed
//    void updateCartItemById(Long id);

}

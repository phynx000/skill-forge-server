package com.skillforge.skillforge_api.repository;

import com.skillforge.skillforge_api.entity.Cart;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {


    Optional<Cart> findByUserId(Long userId);
}

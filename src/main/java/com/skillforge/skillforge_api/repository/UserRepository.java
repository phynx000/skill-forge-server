package com.skillforge.skillforge_api.repository;


import com.skillforge.skillforge_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
}

package com.skillforge.skillforge_api.repository;


import com.skillforge.skillforge_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> , JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
}

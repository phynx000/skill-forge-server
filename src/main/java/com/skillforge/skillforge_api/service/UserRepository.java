package com.skillforge.skillforge_api.service;


import com.skillforge.skillforge_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


}

package com.skillforge.skillforge_api.service;

import com.skillforge.skillforge_api.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsersFromDatabase() {
        List<User> users = userRepository.findAll();
        return users;
    }


}

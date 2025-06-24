package com.skillforge.skillforge_api.controller;

import com.skillforge.skillforge_api.entity.User;
import com.skillforge.skillforge_api.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // api is return all users include student, intructor, admin
    @GetMapping("/api/users")
    public String getAllUser() {
//        List<User> listAllUser = userService.getAllUsersFromDatabase(); // This method should fetch users from the database
        return "User details";
    }



}

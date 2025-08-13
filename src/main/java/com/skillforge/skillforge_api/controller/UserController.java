package com.skillforge.skillforge_api.controller;

import com.skillforge.skillforge_api.dto.request.UserCreateRequest;
import com.skillforge.skillforge_api.dto.request.UserUpdateReq;
import com.skillforge.skillforge_api.dto.response.BioDTO;
import com.skillforge.skillforge_api.dto.response.ResultPaginationDTO;
import com.skillforge.skillforge_api.dto.response.UserDTO;
import com.skillforge.skillforge_api.entity.User;
import com.skillforge.skillforge_api.service.UserService;
import com.skillforge.skillforge_api.utils.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RequestMapping("/api/v1")
@RestController
public class UserController {

    UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    @ApiMessage(value = "fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(
            @Filter Specification<User> specification,
            Pageable pageable) {
        ResultPaginationDTO users = this.userService.getAllUsersFromDatabase(specification,pageable);
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.fetchUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/users")
    @ApiMessage(value = "create a new user successfully")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreateRequest request) {
        String hashPassword = this.passwordEncoder.encode(request.getPassword());
        request.setPassword(hashPassword);
        UserDTO newUserDTO = this.userService.handleCreateUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserDTO);
    }


    @PutMapping("/users")
    @ApiMessage(value = "update user successfully")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserUpdateReq request) {
        UserDTO newUser = this.userService.handleUpdateUser(request);
        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage(value = "delete user successfully")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully");  
    }

    @GetMapping("/bio/{id}")
    @ApiMessage(value = "fetch user bio")
    public ResponseEntity<BioDTO> getUserBio (@PathVariable Long id) {
        BioDTO user = userService.getUserBio(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

}

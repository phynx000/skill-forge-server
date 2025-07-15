package com.skillforge.skillforge_api.dto.mapper;

import com.skillforge.skillforge_api.dto.request.UserCreateRequest;
import com.skillforge.skillforge_api.dto.request.UserUpdateReq;
import com.skillforge.skillforge_api.dto.response.UserDTO;
import com.skillforge.skillforge_api.entity.Role;
import com.skillforge.skillforge_api.entity.User;
import com.skillforge.skillforge_api.utils.constant.GenderEnum;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(UserCreateRequest request) {
        if (request == null) return null;
        User user = new User();
        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());
        user.setAge(request.getAge());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(new Role(3L, "ROLE_STUDENT")); // Assuming role ID 3 is for regular users
        user.setGender(GenderEnum.valueOf(request.getGender()));
        return user;
    }

    public User updateEntity(User currentUser, UserUpdateReq request) {
        if (request == null || request == null) return null;
        String fullName = request.getFullName() != null ? request.getFullName() : currentUser.getFullName();
        currentUser.setFullName(fullName);
        int age = request.getAge() != 0 ? request.getAge() : currentUser.getAge();
        currentUser.setAge(age);
        return currentUser;
    }


    public UserDTO toDto(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setAge(user.getAge());
        dto.setEmail(user.getEmail());
        dto.setGender(user.getGender() != null ? user.getGender().name() : null);
        dto.setRole(user.getRole() != null ? user.getRole().getName() : null);

        return dto;

    }
}

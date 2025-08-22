package com.skillforge.skillforge_api.dto.mapper;

import com.skillforge.skillforge_api.dto.request.UserCreateRequest;
import com.skillforge.skillforge_api.dto.request.UserUpdateReq;
import com.skillforge.skillforge_api.dto.response.UserDTO;
import com.skillforge.skillforge_api.entity.Role;
import com.skillforge.skillforge_api.entity.User;
import com.skillforge.skillforge_api.repository.RoleRepository;
import com.skillforge.skillforge_api.utils.constant.GenderEnum;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final RoleRepository roleRepository;

    public UserMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public User toEntity(UserCreateRequest request) {
        if (request == null) return null;
        User user = new User();
        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());
        user.setAge(request.getAge());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setGender(GenderEnum.valueOf(request.getGender()));
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(Instant.now());
        }
        // Tạm set role mặc định là STUDENT (ID 3)
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(3L, "ROLE_STUDENT"));
        user.setRoles(roles);
        return user;
    }

    public User updateEntity(User currentUser, UserUpdateReq request) {
        if (request == null || request == null) return null;
        String fullName = request.getFullName() != null ? request.getFullName() : currentUser.getFullName();
        currentUser.setFullName(fullName);
        int age = request.getAge() != 0 ? request.getAge() : currentUser.getAge();
        currentUser.setAge(age);
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            Set<Role> roles = roleRepository.findAllById(request.getRoleIds()).stream().collect(Collectors.toSet());
            currentUser.setRoles(roles);
        }
        return currentUser;
    }


    public UserDTO toDto(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        UserDTO.RoleUserDTO roleDto = new UserDTO.RoleUserDTO();

        if (user.getRoles() != null ) {
            Set<String> roleNames = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());
            roleDto.setName(roleNames.stream().collect(Collectors.toList()));
            roleDto.setId(user.getRoles().stream().findFirst().orElse(new Role()).getId());
            dto.setRole(roleDto);

        }

        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setAge(user.getAge());
        dto.setEmail(user.getEmail());
        dto.setGender(user.getGender() != null ? user.getGender().name() : null);

        dto.setSkills(user.getSkills());

        // Lấy tất cả role name (nếu cần nhiều hơn 1)

        return dto;
    }
}

package com.skillforge.skillforge_api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    private int age;
    private String email;
    private String gender;
    private String role;
}

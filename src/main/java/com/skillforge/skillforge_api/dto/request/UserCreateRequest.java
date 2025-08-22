package com.skillforge.skillforge_api.dto.request;

import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserCreateRequest {
    private String username;
    private String fullName;
    private int age;
    private String email;
    private String password;
    private String gender;
    private Instant createdAt;



}

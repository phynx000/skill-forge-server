package com.skillforge.skillforge_api.entity;

import com.skillforge.skillforge_api.utils.constant.GenderEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String fullName;

    @Column(nullable = true)
    private int age;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Column(nullable = false, unique = true)
    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
    private Instant createdAt;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    public User() {
    }



}

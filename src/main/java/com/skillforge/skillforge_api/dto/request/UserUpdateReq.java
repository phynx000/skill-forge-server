package com.skillforge.skillforge_api.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserUpdateReq {
    private Long id;
    private String username;
    private String fullName;
    private int age;
    private List<Long> roleIds;
}

package com.skillforge.skillforge_api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionDTO {
    private Long id;
    private String apiPath;
    private String method;
    private String module;

}

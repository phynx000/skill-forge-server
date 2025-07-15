package com.skillforge.skillforge_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseLoginDTO {
    private String access_token;
    private UserLogin user;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserLogin {
        private Long id;
        private String username;
        private String fullName;
        private String email;

        public UserLogin() {

        }
    }

}

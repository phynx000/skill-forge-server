package com.skillforge.skillforge_api.controller;

import com.skillforge.skillforge_api.dto.response.LoginDTO;
import com.skillforge.skillforge_api.dto.response.ResponseLoginDTO;
import com.skillforge.skillforge_api.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final SecurityUtils securityUtils;
    private final AuthenticationManager authenticationManager;

    public AuthController(SecurityUtils securityUtils, AuthenticationManager authenticationManager) {
        this.securityUtils = securityUtils;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(), loginDTO.getPassword()
                );

        Authentication authentication = authenticationManager.authenticate(authToken);
        this.securityUtils.createToken(authentication);
        String accessToken = this.securityUtils.createToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResponseLoginDTO responseLoginDTO = new ResponseLoginDTO();
        responseLoginDTO.setAccessToken(accessToken);
        return ResponseEntity.ok()
                .body(responseLoginDTO);
    }
}

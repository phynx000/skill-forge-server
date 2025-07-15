package com.skillforge.skillforge_api.controller;

import com.skillforge.skillforge_api.dto.request.LoginRequest;
import com.skillforge.skillforge_api.dto.response.ResponseLoginDTO;
import com.skillforge.skillforge_api.entity.User;
import com.skillforge.skillforge_api.service.UserService;
import com.skillforge.skillforge_api.utils.SecurityUtils;
import com.skillforge.skillforge_api.utils.annotation.ApiMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;



@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final SecurityUtils securityUtils;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private int jwtRefreshExpiration;

    public AuthController(SecurityUtils securityUtils, AuthenticationManager authenticationManager, UserService userService) {
        this.securityUtils = securityUtils;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

        @PostMapping("/auth/login")
        public ResponseEntity<ResponseLoginDTO> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(), loginRequest.getPassword()
                    );
            Authentication authentication = authenticationManager.authenticate(authToken);
            User currenUser = userService.handeFindByEmail(loginRequest.getUsername());

            ResponseLoginDTO.UserLogin userLogin = new ResponseLoginDTO.UserLogin(
                    currenUser.getId(),
                    currenUser.getUsername(),
                    currenUser.getFullName(),
                    currenUser.getEmail()
            );
            ResponseLoginDTO responseLoginDTO = new ResponseLoginDTO();
            responseLoginDTO.setUser(userLogin);

            String accessToken = this.securityUtils.createAccessToken(authentication, responseLoginDTO.getUser());
            responseLoginDTO.setAccess_token(accessToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // create refresh token
            String refreshToken = this.securityUtils.createRefreshToken(loginRequest.getUsername(), responseLoginDTO );
            this.userService.updateUserToken(refreshToken, loginRequest.getUsername());
            ResponseCookie responseCookie = ResponseCookie.from(("refresh_token"), refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .sameSite("Strict")
                    .maxAge(jwtRefreshExpiration) // 30 days
                    .build();
            return ResponseEntity.ok().header("Set-Cookie", responseCookie.toString())
                    .body(responseLoginDTO);
        }

        @GetMapping("/auth/account")
        @ApiMessage("Get current user account information")
    public ResponseEntity<ResponseLoginDTO.UserLogin> getCurrentUser() {
        String email = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        User currentUser = userService.handeFindByEmail(email);
        ResponseLoginDTO.UserLogin userLogin = new ResponseLoginDTO.UserLogin();
        if (currentUser != null) {
            userLogin.setId(currentUser.getId());
            userLogin.setFullName(currentUser.getFullName());
            userLogin.setEmail(currentUser.getEmail());
        }
        return ResponseEntity.ok(userLogin);
    }

    @PostMapping("/auth/logout")
    @ApiMessage("Logs out the current authenticated user")
    public ResponseEntity<Void> logout() {
        // Xóa toàn bộ SecurityContext, bao gồm cả đối tượng Authentication
        SecurityContextHolder.clearContext();

        // Tạo một cookie hết hạn ngay lập tức để xóa refresh_token phía client
        ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(0) // Hết hạn ngay lập- tức
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .build();
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Refreshes the access token using the refresh token stored in cookies")
    public ResponseEntity<String> getRefreshToken(HttpServletRequest request,  @CookieValue(name = "refresh_token") String refresh_token ) {

       Jwt decoded = this.securityUtils.checkValidRefreshToken(refresh_token);

        // Trả về access token mới trong response header
        return ResponseEntity.ok()
                .header(null)
                .body(refresh_token);

    }


}

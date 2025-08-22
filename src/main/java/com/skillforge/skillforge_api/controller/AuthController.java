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
                    currenUser.getEmail(),
                    currenUser.getRoles().stream()
                            .map(role -> role.getName())
                            .toList()
            );
            ResponseLoginDTO responseLoginDTO = new ResponseLoginDTO();
            responseLoginDTO.setUser(userLogin);

            String accessToken = this.securityUtils.createAccessToken(authentication.getName(), responseLoginDTO);
            responseLoginDTO.setAccessToken(accessToken);

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
    public ResponseEntity<ResponseLoginDTO.UserGetAccount> getCurrentUser() {
        String email = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        User currentUser = userService.handeFindByEmail(email);
        ResponseLoginDTO.UserLogin userLogin = new ResponseLoginDTO.UserLogin();
        ResponseLoginDTO.UserGetAccount userGetAccount = new ResponseLoginDTO.UserGetAccount(userLogin);
        if (currentUser != null) {
            userLogin.setId(currentUser.getId());
            userLogin.setFullName(currentUser.getFullName());
            userLogin.setEmail(currentUser.getEmail());
            userGetAccount.setUser(userLogin);
        }
        return ResponseEntity.ok(userGetAccount);
    }

    @PostMapping("/auth/logout")
    @ApiMessage("Logs out the current authenticated user")
    public ResponseEntity<Void> logout() {
        String email = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        if (email.isEmpty()) {
            throw new IllegalArgumentException("User is not authenticated");
        }

        this.userService.updateUserToken(null, email);

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
    public ResponseEntity<ResponseLoginDTO> getRefreshToken(HttpServletRequest request,
                                                            @CookieValue(name = "refresh_token", defaultValue = "none") String refresh_token) {

        if (refresh_token.equals("none")) {
            throw new IllegalArgumentException("Refresh token is required");
        }
        // check valid token
       Jwt decoded = this.securityUtils.checkValidRefreshToken(refresh_token);
       String email = decoded.getSubject();

        User currentUser =  this.userService.findUserByTRefreshTokenAndEmail(refresh_token, email);
        if (currentUser == null) {
            return ResponseEntity.badRequest().build();
        }

        ResponseLoginDTO.UserLogin userLogin = new ResponseLoginDTO.UserLogin(
                currentUser.getId(),
                currentUser.getUsername(),
                currentUser.getFullName(),
                currentUser.getEmail(),
                currentUser.getRoles().stream()
                        .map(role -> role.getName())
                        .toList()
        );

        ResponseLoginDTO responseLoginDTO = new ResponseLoginDTO();
        responseLoginDTO.setUser(userLogin);

        String accessToken = this.securityUtils.createAccessToken(email, responseLoginDTO);
        responseLoginDTO.setAccessToken(accessToken);

        // create refresh token
        String new_refreshToken = this.securityUtils.createRefreshToken(email, responseLoginDTO );
        this.userService.updateUserToken(new_refreshToken, email);
        ResponseCookie responseCookie = ResponseCookie.from(("refresh_token"), new_refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(jwtRefreshExpiration) // 30 days
                .build();
        return ResponseEntity.ok().header("Set-Cookie", responseCookie.toString())
                .body(responseLoginDTO);

    }


}

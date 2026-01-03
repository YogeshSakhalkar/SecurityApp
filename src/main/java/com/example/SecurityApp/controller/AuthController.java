package com.example.SecurityApp.controller;

import com.example.SecurityApp.dto.LoginDto;
import com.example.SecurityApp.dto.SignUpDto;
import com.example.SecurityApp.dto.UserDto;
import com.example.SecurityApp.service.AuthService;
import com.example.SecurityApp.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignUpDto signUpDto){
        UserDto userDto = authService.signUp(signUpDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto,
                                        HttpServletRequest request, HttpServletResponse response){
        String token = authService.login(loginDto);

        Cookie cookie = new Cookie("token",token);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return ResponseEntity.ok(token);
    }
}

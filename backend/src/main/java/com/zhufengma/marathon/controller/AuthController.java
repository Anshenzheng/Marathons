package com.zhufengma.marathon.controller;

import com.zhufengma.marathon.dto.ApiResponse;
import com.zhufengma.marathon.dto.LoginRequest;
import com.zhufengma.marathon.dto.LoginResponse;
import com.zhufengma.marathon.dto.RegisterRequest;
import com.zhufengma.marathon.entity.User;
import com.zhufengma.marathon.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = authService.register(request);
            user.setPassword(null); // 隐藏密码
            return ResponseEntity.ok(ApiResponse.success("注册成功", user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(ApiResponse.success("登录成功", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}

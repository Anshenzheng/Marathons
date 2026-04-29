package com.zhufengma.marathon.service;

import com.zhufengma.marathon.dto.LoginRequest;
import com.zhufengma.marathon.dto.LoginResponse;
import com.zhufengma.marathon.dto.RegisterRequest;
import com.zhufengma.marathon.entity.Admin;
import com.zhufengma.marathon.entity.User;
import com.zhufengma.marathon.repository.AdminRepository;
import com.zhufengma.marathon.repository.UserRepository;
import com.zhufengma.marathon.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Transactional
    public User register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查身份证号是否已存在
        if (userRepository.existsByIdCard(request.getIdCard())) {
            throw new RuntimeException("身份证号已存在");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setIdCard(request.getIdCard());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setGender(User.Gender.valueOf(request.getGender().toLowerCase()));
        user.setBirthDate(request.getBirthDate());
        user.setEmergencyContact(request.getEmergencyContact());
        user.setEmergencyPhone(request.getEmergencyPhone());

        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 先尝试查找用户
        User user = userRepository.findByUsernameAndIsDeleted(request.getUsername(), 0).orElse(null);
        if (user != null) {
            String token = jwtTokenUtil.generateToken(user.getUsername(), "user", user.getId());
            return new LoginResponse(token, user.getUsername(), "user", user.getId(), user.getRealName());
        }

        // 再尝试查找管理员
        Admin admin = adminRepository.findByUsernameAndIsDeleted(request.getUsername(), 0).orElse(null);
        if (admin != null) {
            String token = jwtTokenUtil.generateToken(admin.getUsername(), "admin", admin.getId());
            return new LoginResponse(token, admin.getUsername(), "admin", admin.getId(), admin.getRealName());
        }

        throw new RuntimeException("用户不存在");
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    public Admin getAdminById(Long id) {
        return adminRepository.findById(id).orElseThrow(() -> new RuntimeException("管理员不存在"));
    }
}

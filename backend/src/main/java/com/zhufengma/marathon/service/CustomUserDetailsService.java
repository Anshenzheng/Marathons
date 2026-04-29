package com.zhufengma.marathon.service;

import com.zhufengma.marathon.entity.Admin;
import com.zhufengma.marathon.entity.User;
import com.zhufengma.marathon.repository.AdminRepository;
import com.zhufengma.marathon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 先尝试查找用户
        User user = userRepository.findByUsernameAndIsDeleted(username, 0).orElse(null);
        if (user != null) {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    authorities
            );
        }

        // 再尝试查找管理员
        Admin admin = adminRepository.findByUsernameAndIsDeleted(username, 0).orElse(null);
        if (admin != null) {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            if (admin.getRole() == Admin.Role.super_admin) {
                authorities.add(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
            }
            return new org.springframework.security.core.userdetails.User(
                    admin.getUsername(),
                    admin.getPassword(),
                    authorities
            );
        }

        throw new UsernameNotFoundException("用户不存在: " + username);
    }
}

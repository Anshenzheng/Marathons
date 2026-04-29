package com.zhufengma.marathon.repository;

import com.zhufengma.marathon.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
    Optional<Admin> findByUsernameAndIsDeleted(String username, Integer isDeleted);
    boolean existsByUsername(String username);
}

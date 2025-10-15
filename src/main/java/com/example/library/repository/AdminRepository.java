package com.example.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.library.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByEmail(String email);
    boolean existsByAdminId(String adminId);

    // New method to fetch admin by adminId
    Admin findByAdminId(String adminId);
}

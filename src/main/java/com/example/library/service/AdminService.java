package com.example.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.library.model.Admin;
import com.example.library.repository.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ---------------- Register Admin ----------------
    @Transactional
    public Admin registerAdmin(Admin admin) {
        if (admin == null) {
            throw new IllegalArgumentException("Admin must not be null");
        }
        if (admin.getEmail() == null || admin.getEmail().isBlank()) {
            throw new IllegalArgumentException("Admin email must be provided");
        }
        if (admin.getAdminId() == null || admin.getAdminId().isBlank()) {
            throw new IllegalArgumentException("Admin ID must be provided");
        }

        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new IllegalArgumentException("Email already registered!");
        }
        if (adminRepository.existsByAdminId(admin.getAdminId())) {
            throw new IllegalArgumentException("Admin ID already taken!");
        }

        // Hash password securely
        if (admin.getPassword() != null && !admin.getPassword().isBlank()) {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        }

        return adminRepository.save(admin);
    }

    // ---------------- Validate Admin Login ----------------
    public boolean validateAdmin(String adminId, String password) {
        if (adminId == null || adminId.isBlank() || password == null) {
            return false;
        }

        Admin admin = adminRepository.findByAdminId(adminId);
        if (admin == null || admin.getPassword() == null) {
            return false;
        }

        // Compare encoded password securely
        return passwordEncoder.matches(password, admin.getPassword());
    }
}

package com.example.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.library.model.User;
import com.example.library.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Using BCrypt for password security
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ---------- Register New User ----------
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }
        if (userRepository.existsByStudentId(user.getStudentId())) {
            throw new RuntimeException("Student ID already registered!");
        }

        // Encrypt the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // ---------- Authenticate Login ----------
   public User authenticate(String email, String password) {
    User user = userRepository.findByEmail(email);
    if (user == null) return null;
    return user.getPassword() != null && user.getPassword().equals(password) ? user : null;
}

}
